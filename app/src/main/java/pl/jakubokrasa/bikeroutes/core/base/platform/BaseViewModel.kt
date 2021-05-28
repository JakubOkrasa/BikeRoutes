package pl.jakubokrasa.bikeroutes.core.base.platform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import org.koin.java.KoinJavaComponent.inject
import pl.jakubokrasa.bikeroutes.core.app.domain.IsUserSignedInUseCase

open class BaseViewModel: ViewModel() {

    protected val _uiState by lazy {
        MutableLiveData<UiState>(
            UiState.Idle
        )
    }

    protected val _message by lazy {
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

    protected fun showMessage(message: String) {
        _message.value = message
    }
}