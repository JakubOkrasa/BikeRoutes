package pl.jakubokrasa.bikeroutes.core.user.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.user.domain.DataSignIn
import pl.jakubokrasa.bikeroutes.core.user.domain.GetUserUseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.IsUserSignedInUseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.SignInUseCase
import pl.jakubokrasa.bikeroutes.core.util.PreferenceHelper

class MainViewModel(
    private val preferenceHelper: PreferenceHelper,
    private val isUserEmailSignedInUseCase: IsUserSignedInUseCase,
    private val signInUseCase: SignInUseCase,
    private val getUserUseCase: GetUserUseCase,
): BaseViewModel() {
    override val LOG_TAG: String = MainViewModel::class.simpleName ?: "unknown"
    private val _isSignedIn by lazy { MutableLiveData<Boolean>()
        .also { it.value = isUserSignedIn() }
    }
    private val _startActivity by lazy { LiveEvent<Boolean>() }

    val isSignedIn: LiveData<Boolean> by lazy { _isSignedIn }
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
        setPendingState()
        signInUseCase(
            params = DataSignIn(email, password),
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess { authResult ->
                authResult.uid?.let {
                    getUser(it)
                    preferenceHelper.saveUserDataToSharedPreferences(email, password, it)
                }
                _isSignedIn.value = true
                handleSuccess("signIn")
            }
            result.onFailure {
                handleFailure("signIn", it.message ?: "Sign in failed")
                _startActivity.value = true

            }
        }
    }

    private fun getUser(uid: String) {
        setPendingState()
        getUserUseCase(
            params = uid,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                preferenceHelper.saveDisplayNameToSharedPreferences(it.displayName)
                handleSuccess("getUser")
            }
            result.onFailure {
                handleFailure("getUser", it.message ?: "Sign in failed")
            }
        }
    }
}