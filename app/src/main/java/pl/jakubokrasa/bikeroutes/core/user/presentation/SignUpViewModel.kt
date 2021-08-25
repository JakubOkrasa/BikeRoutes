package pl.jakubokrasa.bikeroutes.core.user.presentation

import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserUseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.GetUserUseCase
import pl.jakubokrasa.bikeroutes.core.util.PreferenceHelper

class SignUpViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val preferenceHelper: PreferenceHelper
): BaseViewModel() {
    override val LOG_TAG: String = SignUpViewModel::class.simpleName?: "unknown"

        private val _startActivity by lazy { LiveEvent<Boolean>() }
    val startActivity by lazy { _startActivity }

    fun createUser(email: String, password: String, displayName: String) {
        setPendingState()
        createUserUseCase(
            params = CreateUserData(email, password, displayName),
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess { authResult ->
                authResult.uid?.let {
                    getUser(it)
                    preferenceHelper.saveUserDataToSharedPreferences(email, password, it)
                }
                _startActivity.value = true
                handleSuccess("createUser")
            }
            result.onFailure {
                handleFailure("createUser", it.message ?: "user wasn't created")
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
                handleFailure("getUser", it.message ?: "couldn't get user info")
            }
        }
    }

    companion object {
        val LOG_TAG = SignUpViewModel::class.simpleName
    }
}