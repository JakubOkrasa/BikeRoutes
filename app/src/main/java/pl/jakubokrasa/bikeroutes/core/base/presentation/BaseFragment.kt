package pl.jakubokrasa.bikeroutes.core.base.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.core.util.PreferenceHelper

abstract class BaseFragment<T: BaseViewModel>(@LayoutRes layoutRes: Int): Fragment(layoutRes) {
    protected abstract val viewModel: T
    protected val preferenceHelper: PreferenceHelper by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    open fun initObservers() {
        observeUiState()
        observeMessage()
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner, {
            when(it) {
                UiState.Idle -> onIdleState()
                UiState.Pending -> onPendingState()
            }
        })
    }

    private fun observeMessage() {
        viewModel.message.observe(viewLifecycleOwner, {
            showToast(it)
        })
    }

    protected open fun onPendingState() {}

    protected open fun onIdleState() {}

    protected fun showToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }
}