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

    private val _startActivity by lazy { LiveEvent<Boolean>() }
    val startActivity by lazy { _startActivity }

    fun resetPassword(email: String) {
        resetPasswordUseCase(
            params = email,
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess {
                handleSuccess("resetPassword", "Reset password message was sent.")
            }
            result.onFailure {
                handleFailure("resetPassword", it.message ?: "Reset password message wasn't sent") }
        }
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
}