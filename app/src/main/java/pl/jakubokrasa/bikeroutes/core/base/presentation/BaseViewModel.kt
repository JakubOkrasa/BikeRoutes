package pl.jakubokrasa.bikeroutes.core.base.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent

abstract class BaseViewModel: ViewModel() {
    protected abstract val LOG_TAG: String

    private val _uiState by lazy {
        MutableLiveData<UiState>(
            UiState.Idle
        )
    }

    private val _message by lazy {
        LiveEvent<String>()
    }

    val uiState: LiveData<UiState> by lazy { _uiState }

    val message: LiveData<String> by lazy { _message }

    protected fun setIdleState() {
        _uiState.value =
            UiState.Idle
    }

    protected fun setPendingState() {
        _uiState.value =
            UiState.Pending
    }

    private fun showMessage(message: String) {
        _message.value = message
    }

    protected fun handleSuccess(methodName: String, msg: String = "") {
        Log.d(LOG_TAG, "onSuccess $methodName")
        if (msg.isNotEmpty())
            showMessage(msg)
    }

    protected fun handleFailure(methodName: String, msg: String = "", errLog: String?="") {
        Log.e(LOG_TAG, "onFailure $methodName $errLog")
        if (msg.isNotEmpty())
            showMessage("Error: $msg")
    }
}