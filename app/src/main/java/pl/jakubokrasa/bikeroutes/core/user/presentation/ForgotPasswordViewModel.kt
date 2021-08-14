package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserUseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.ResetPasswordUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

class ForgotPasswordViewModel(
    private val resetPasswordUseCase: ResetPasswordUseCase,
): BaseViewModel() {
    override val LOG_TAG: String = ForgotPasswordViewModel::class.simpleName?: "unknown"

    fun resetPassword(email: String) {
        setPendingState()
        resetPasswordUseCase(
            params = email,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess { handleSuccess("resetPassword", "Reset password message was sent.") }
            result.onFailure { handleFailure("resetPassword", it.message ?: "Reset password message wasn't sent", errLog = it.message) }
        }
    }
}