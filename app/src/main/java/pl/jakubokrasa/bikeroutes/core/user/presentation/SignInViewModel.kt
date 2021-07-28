package pl.jakubokrasa.bikeroutes.core.user.presentation

import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.DataSignIn
import pl.jakubokrasa.bikeroutes.core.user.domain.SignInUseCase

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val preferenceHelper: PreferenceHelper
): BaseViewModel() {

    override val LOG_TAG: String = SignInViewModel::class.simpleName ?: "unknown"
    private val _startActivity by lazy { LiveEvent<Boolean>() }
    val startActivity by lazy { _startActivity }

    fun signIn(email: String, password: String) {
        signInUseCase(
            params = DataSignIn(email, password),
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess { authResult ->
                authResult.uid?.let {
                    preferenceHelper.saveUserDataToSharedPreferences(email, password, it)
                }
                _startActivity.value = true
                handleSuccess("signIn")
            }
            result.onFailure {
                handleFailure("signIn", it.message ?: "Sign in failed")
            }
        }
    }
}