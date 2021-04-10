package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserUseCase

class UserViewModel(
    private val createUserUseCase: CreateUserUseCase,
): ViewModel() {

    fun createUser(email: String) {
        createUserUseCase(
            params = email,
            scope = viewModelScope
        ) {
                result -> result.onSuccess { Log.d(LOG_TAG, "user created")}
            result.onFailure { Log.e(LOG_TAG, "user not created") }
        }
    }

    companion object {
        val LOG_TAG = UserViewModel::class.simpleName
    }
}