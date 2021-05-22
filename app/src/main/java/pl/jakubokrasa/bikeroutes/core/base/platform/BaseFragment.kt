package pl.jakubokrasa.bikeroutes.core.base.platform

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper

abstract class BaseFragment<T: BaseViewModel>(@LayoutRes layoutRes: Int): Fragment(layoutRes) {
    abstract val viewModel: T
    protected val preferenceHelper: PreferenceHelper by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    open fun initViews() {}

    open fun initObservers() {
        observeUiState()
        observeMessage()
    }

    fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when(it) {
                UiState.Idle -> onIdleState()
                UiState.Pending -> onPendingState()
            }
        })
    }

    fun observeMessage() {
        viewModel.message.observe(viewLifecycleOwner, {
            showToast(it)
        })
    }

    protected open fun onPendingState() {}

    protected open fun onIdleState() {}

    protected fun showToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    // TODO: 4/6/2021 UIState, Message livedata for errors
}