package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import org.koin.ext.scope
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserUseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.DeleteCurrentUserUseCase

class UserViewModel(
    private val preferenceHelper: PreferenceHelper,
    private val createUserUseCase: CreateUserUseCase,
    private val deleteCurrentCurrentUserUseCase: DeleteCurrentUserUseCase
): ViewModel() {

    private val _message by lazy { LiveEvent<Pair<Boolean, String>>() }
    val message: LiveData<Pair<Boolean, String>> by lazy { _message }

    fun createUser(email: String, password: String) {
        createUserUseCase(
            params = CreateUserData(email, password),
            scope = viewModelScope
        ) {
            result ->
                result.onSuccess {
                    Log.d(LOG_TAG, "user created")
                    saveUserDataToSharedPreferences(email, password)
                    showMessage(true, "Successfully Registered")
                }
                result.onFailure {
                    Log.e(LOG_TAG, "user not created, " + it.message)
                    showMessage(false, "Registration Failed: " + it.message)
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
                    deleteUserDataFromSharedPreferences()
                    showMessage(true, "Successfully Registered")
                }
                result.onFailure {
                    Log.e(LOG_TAG, "user not deleted, " + it.message)
                    showMessage(false, "An error occured while deleting account")
                }
        }
    }

    private fun showMessage(isSuccess: Boolean, msg: String) {
        _message.value = Pair(isSuccess, msg)
    }

    private fun saveUserDataToSharedPreferences(email: String, password: String) {
        preferenceHelper.preferences.edit {
            putString(PreferenceHelper.PREF_KEY_USER_EMAIL, email)
            putString(PreferenceHelper.PREF_KEY_USER_PASSWORD, password)
        }
    }

    private fun deleteUserDataFromSharedPreferences() {
        preferenceHelper.preferences.edit {
                remove(PreferenceHelper.PREF_KEY_USER_EMAIL)
                remove(PreferenceHelper.PREF_KEY_USER_PASSWORD)
            }
    }

    companion object {
        val LOG_TAG = UserViewModel::class.simpleName
    }
}