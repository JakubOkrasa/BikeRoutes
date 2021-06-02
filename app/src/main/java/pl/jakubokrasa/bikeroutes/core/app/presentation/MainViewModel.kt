package pl.jakubokrasa.bikeroutes.core.app.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.app.domain.IsUserSignedInUseCase
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.DataSignIn
import pl.jakubokrasa.bikeroutes.core.user.domain.SignInUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

class MainViewModel(
    private val preferenceHelper: PreferenceHelper,
    private val isUserEmailSignedInUseCase: IsUserSignedInUseCase,
    private val signInUseCase: SignInUseCase,
    private val mainNavigator: MainNavigator,
): BaseViewModel() { //todo isUserSignedIN in BaseVM
    override val LOG_TAG: String = MainViewModel::class.simpleName ?: "unknown"
    private val _isSignedIn by lazy { MutableLiveData<Boolean>()
        .also {
            it.value = isUserSignedIn()
        }}
    val isSignedIn: LiveData<Boolean> by lazy { _isSignedIn }

    private val _startActivity by lazy { LiveEvent<Boolean>() }
    val startActivity by lazy { _startActivity }

    fun isUserSignedIn(): Boolean {
        var signedIn = false
        isUserEmailSignedInUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result ->
            result.onSuccess {
                handleSuccess("isUserSignedIn")
                signedIn = it
            }
            result.onFailure { handleFailure("isUserSignedIn") }
        }
        return signedIn
    }

    fun signIn(email: String, password: String) {
        signInUseCase(
            params = DataSignIn(email, password),
            scope = viewModelScope
        ) {
                result ->
            setPendingState()
            result.onSuccess {
                preferenceHelper.saveUserDataToSharedPreferences(email, password)
                _isSignedIn.value = true
                handleSuccess("signIn")
            }
            result.onFailure {
                handleFailure("signIn", it.message ?: "Sign in failed")
                _startActivity.value = true

            }
        }
    }
}