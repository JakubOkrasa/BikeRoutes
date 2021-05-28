package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserUseCase

class SignUpViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val preferenceHelper: PreferenceHelper
): ViewModel() {

    private val _startActivity by lazy { LiveEvent<Boolean>() }
    val startActivity by lazy { _startActivity }

    fun createUser(email: String, password: String) {
        createUserUseCase(
            params = CreateUserData(email, password),
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess {
                Log.d(UserViewModel.LOG_TAG, "user created")
                preferenceHelper.saveUserDataToSharedPreferences(email, password)
                _startActivity.value = true
//                handleSuccess("createUser")
            }
            result.onFailure {
//                handleFailure("createUser", it.message ?: "user wasn't created")
            }
        }
    }
}