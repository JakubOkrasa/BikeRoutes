package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.*
import pl.jakubokrasa.bikeroutes.core.user.navigation.UserNavigator
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

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
        deleteCurrentCurrentUserUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result ->
                result.onSuccess {
                    handleSuccess("deleteCurrentUser", "account was deleted")
                    preferenceHelper.deleteUserDataFromSharedPreferences()
                }
                result.onFailure { handleFailure("deleteCurrentUser", "couldn't delete the account", errLog = it.message) }
        }
    }

    fun logOut() {
        logOutUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
                result ->
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