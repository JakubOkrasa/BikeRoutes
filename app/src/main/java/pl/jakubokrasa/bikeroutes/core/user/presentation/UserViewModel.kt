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
    private val createUserUseCase: CreateUserUseCase,
    private val deleteCurrentCurrentUserUseCase: DeleteCurrentUserUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val signInUseCase: SignInUseCase,
    private val logOutUseCase: LogOutUseCase,

    private val userNavigator: UserNavigator,
): BaseViewModel() {

    fun createUser(email: String, password: String) {
        createUserUseCase(
            params = CreateUserData(email, password),
            scope = viewModelScope
        ) {
            result ->
                result.onSuccess {
                    Log.d(LOG_TAG, "user created")
                    preferenceHelper.saveUserDataToSharedPreferences(email, password)
                    userNavigator.signUpToMap()
                    handleSuccess("createUser")
                }
                result.onFailure {
                    handleFailure("createUser", it.message ?: "user wasn't created")
                }
        }
    }

    fun deleteCurrentUser() {
        deleteCurrentCurrentUserUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result ->
                result.onSuccess {
                    Log.d(LOG_TAG, "user deleted")
                    preferenceHelper.deleteUserDataFromSharedPreferences()
                    showMessage("Successfully Registered")
                }
                result.onFailure {
                    Log.e(LOG_TAG, "user not deleted, " + it.message)
                    showMessage("An error occured while deleting account")
                }
        }
    }

    fun resetPassword(email: String) {
        resetPasswordUseCase(
            params = email,
            scope = viewModelScope
        ) {
            result ->
            setPendingState()
            result.onSuccess { handleSuccess("resetPassword", "Reset password message was sent.") }
            result.onFailure { handleFailure("resetPassword", it.message ?: "Reset password message wasn't sent") }
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
                userNavigator.accountToSignIn()
            }
            result.onFailure { handleFailure("logOut", it.message ?: "You weren't logged out") }
        }
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
                userNavigator.signInToMap()
                handleSuccess("signIn")
            }
            result.onFailure {
                handleFailure("signIn", it.message ?: "Sign in failed")
            }
        }
    }

    fun navBack() {
        userNavigator.goBack()
    }

    private fun navFromSignInToMap() {
        userNavigator.signInToMap()
    }

    private fun navFromSignUpToMap() {
        userNavigator.signInToMap()
    }

    fun navFromSignUpToSignIn() {
        userNavigator.signUpToSignIn()
    }

    fun navFromSignInToSignUp() {
        userNavigator.signInToSignUp()
    }


    private fun handleSuccess(methodName: String, msg: String = "") {
        Log.d(MyRoutesViewModel.LOG_TAG, "onSuccess $methodName")
        if (msg.isNotEmpty())
            showMessage(msg)
    }

    private fun handleFailure(methodName: String, msg: String = "", errLog: String?="") {
        Log.e(MyRoutesViewModel.LOG_TAG, "onFailure $methodName $errLog")
        if (msg.isNotEmpty())
            showMessage("Error: $msg")
    }

    companion object {
        val LOG_TAG = UserViewModel::class.simpleName
    }
}