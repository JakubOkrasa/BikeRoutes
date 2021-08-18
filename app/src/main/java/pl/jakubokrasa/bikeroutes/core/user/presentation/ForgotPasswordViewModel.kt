package pl.jakubokrasa.bikeroutes.core.user.presentation

import androidx.lifecycle.viewModelScope
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.user.domain.ResetPasswordUseCase

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