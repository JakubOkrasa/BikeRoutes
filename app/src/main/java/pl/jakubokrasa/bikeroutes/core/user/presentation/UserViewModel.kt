package pl.jakubokrasa.bikeroutes.core.user.presentation

import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.user.domain.DeleteCurrentUserUseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.LogOutUseCase
import pl.jakubokrasa.bikeroutes.core.util.PreferenceHelper

class UserViewModel(
    private val preferenceHelper: PreferenceHelper,
    private val deleteCurrentCurrentUserUseCase: DeleteCurrentUserUseCase,
    private val logOutUseCase: LogOutUseCase,
): BaseViewModel() {

    override val LOG_TAG: String = UserViewModel::class.simpleName?: "unknown"

    private val _startActivity by lazy { LiveEvent<Boolean>() }
    val startActivity by lazy { _startActivity }

    //TODO("To use in the future release")
    fun deleteCurrentUser() {
        setPendingState()
        deleteCurrentCurrentUserUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result ->
            setIdleState()
                result.onSuccess {
                    handleSuccess("deleteCurrentUser", "account was deleted")
                    preferenceHelper.deleteUserDataFromSharedPreferences()
                }
                result.onFailure { handleFailure("deleteCurrentUser", "couldn't delete the account", errLog = it.message) }
        }
    }

    fun logOut() {
        setPendingState()
        logOutUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            setPendingState()
            result.onSuccess {
                handleSuccess("logOut")
                preferenceHelper.deleteUserDataFromSharedPreferences()
                _startActivity.value = true
            }
            result.onFailure { handleFailure("logOut", it.message ?: "You weren't logged out") }
        }
    }
}