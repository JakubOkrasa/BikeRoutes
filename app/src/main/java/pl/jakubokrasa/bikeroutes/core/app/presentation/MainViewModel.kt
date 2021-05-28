package pl.jakubokrasa.bikeroutes.core.app.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.app.domain.IsUserSignedInUseCase
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.DataSignIn
import pl.jakubokrasa.bikeroutes.core.user.domain.LogOutUseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.SignInUseCase
import pl.jakubokrasa.bikeroutes.core.user.navigation.UserNavigator
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

class MainViewModel(
    private val preferenceHelper: PreferenceHelper,
    private val isUserEmailSignedInUseCase: IsUserSignedInUseCase,
    private val signInUseCase: SignInUseCase,
    private val mainNavigator: MainNavigator,
): BaseViewModel() { //todo isUserSignedIN in BaseVM

    private val _isSignedIn by lazy { MutableLiveData<Boolean>()
        .also {
            isUserSignedIn(it)
        }}
    val isSignedIn: LiveData<Boolean> by lazy { _isSignedIn }

    private fun isUserSignedIn(isSignedInLiveData: MutableLiveData<Boolean>) {
        isUserEmailSignedInUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result ->
            result.onSuccess {
                handleSuccess("isUserSignedIn")
                isSignedInLiveData.value = it
            }
            result.onFailure { handleFailure("isUserSignedIn") }
        }
    }

    fun signIn(email: String, password: String) {
        signInUseCase(
            params = DataSignIn(email, password),
            scope = viewModelScope
        ) {
                result ->
            setPendingState()
            result.onSuccess {
                preferenceHelper.saveUserDataToSharedPreferences(email, password)
                _isSignedIn.value = true
                handleSuccess("signIn")
            }
            result.onFailure {
                handleFailure("signIn", it.message ?: "Sign in failed")
                mainNavigator.navigateTo(R.layout.fragment_sign_in)

            }
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