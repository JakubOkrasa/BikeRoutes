package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

class SignUpViewModel(
    private val createUserUseCase: CreateUserUseCase,
    private val preferenceHelper: PreferenceHelper
): BaseViewModel() {
    override val LOG_TAG: String = SignUpViewModel::class.simpleName?: "unknown"

        private val _startActivity by lazy { LiveEvent<Boolean>() }
    val startActivity by lazy { _startActivity }

    fun createUser(email: String, password: String) {
        createUserUseCase(
            params = CreateUserData(email, password),
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess { authResult ->
                authResult.uid?.let {
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

    companion object {
        val LOG_TAG = SignUpViewModel::class.simpleName
    }
}