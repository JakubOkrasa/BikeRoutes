package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.DataSignIn
import pl.jakubokrasa.bikeroutes.core.user.domain.SignInUseCase

class SignInViewModel(
    private val signInUseCase: SignInUseCase,
    private val preferenceHelper: PreferenceHelper
): ViewModel() {

    private val _startActivity by lazy { LiveEvent<Boolean>() }
    val startActivity by lazy { _startActivity }

    fun signIn(email: String, password: String) {
        signInUseCase(
            params = DataSignIn(email, password),
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess {
                preferenceHelper.saveUserDataToSharedPreferences(email, password)
                _startActivity.value = true
//                handleSuccess("signIn")
            }
            result.onFailure {
//                handleFailure("signIn", it.message ?: "Sign in failed")
            }
        }
    }
}