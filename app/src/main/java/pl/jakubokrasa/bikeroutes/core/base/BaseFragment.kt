package pl.jakubokrasa.bikeroutes.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.compat.SharedViewModelCompat.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.inject
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteViewModel

open class BaseFragment(@LayoutRes layoutRes: Int): Fragment(layoutRes) {
    protected val viewModel: RouteViewModel by sharedViewModel()
    protected val preferenceHelper: PreferenceHelper by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()
    }

    open fun initViews() {}

    open fun initObservers() {}

    protected fun showToast(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    // TODO: 4/6/2021 UIState, Message livedata for errors
}