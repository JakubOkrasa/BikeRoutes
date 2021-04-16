package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserUseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.SaveUserToFirestoreUseCase

class UserViewModel(
    private val preferenceHelper: PreferenceHelper,
    private val createUserUseCase: CreateUserUseCase,
    private val saveUserToFirestoreUseCase: SaveUserToFirestoreUseCase
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
                    saveUserToFirestore(it)
                    saveUserDataToSharedPreferences(email, password)
                    _message.value = Pair(true, "Successfully Registered")
                    //goto main activity using nav component
                }
                result.onFailure {
                    Log.e(LOG_TAG, "user not created, " + it.message)
                    _message.value = Pair(false, "Registration Failed: " + it.message)
                }
        }
    }

    private fun saveUserToFirestore(uid: String) {
        saveUserToFirestoreUseCase(
            params = uid,
            scope = viewModelScope
        ) {
            result ->
                result.onSuccess {  }
                result.onFailure {  }
        }
    }

    private fun saveUserDataToSharedPreferences(email: String, password: String) {
        preferenceHelper.preferences.edit {
            putString(PreferenceHelper.PREF_KEY_USER_EMAIL, email)
            putString(PreferenceHelper.PREF_KEY_USER_PASSWORD, password)
        }
    }

    companion object {
        val LOG_TAG = UserViewModel::class.simpleName
    }
}