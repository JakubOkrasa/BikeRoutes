package pl.jakubokrasa.bikeroutes.core.app.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import pl.jakubokrasa.bikeroutes.core.app.domain.IsUserSignedInUseCase
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

class MainViewModel(
    private val isUserEmailSignedInUseCase: IsUserSignedInUseCase
): BaseViewModel() {

    fun isUserSignedIn(): Boolean {
        var isSignedIn: Boolean = false
        isUserEmailSignedInUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result ->
            result.onSuccess {
                handleSuccess("isUserSignedIn")
                isSignedIn = it
            }
            result.onFailure { handleFailure("isUserSignedIn") }
        }
        return isSignedIn
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