package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.jakubokrasa.bikeroutes.MainActivity
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserUseCase

class UserViewModel(
    private val preferenceHelper: PreferenceHelper,
    private val createUserUseCase: CreateUserUseCase,
): ViewModel() {

    fun createUser(email: String, password: String) {
        createUserUseCase(
            params = CreateUserData(email, password),
            scope = viewModelScope
        ) {
            result ->
                result.onSuccess {
                    Log.d(LOG_TAG, "user created")
                    preferenceHelper.preferences.edit {
                        putString(PreferenceHelper.PREF_KEY_USER_EMAIL, email)
                        putString(PreferenceHelper.PREF_KEY_USER_PASSWORD, password)
                    }
//                    Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }
                result.onFailure {
                    Log.e(LOG_TAG, "user not created")
//                    Toast.makeText(this, "Registration Failed. Possible cause: Password must have at least 6 characters", Toast.LENGTH_LONG).show()
                }
        }
    }

    companion object {
        val LOG_TAG = UserViewModel::class.simpleName
    }
}