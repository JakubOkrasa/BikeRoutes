package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.FragmentMyRoutesBinding
import pl.jakubokrasa.bikeroutes.features.myroutes.navigation.MyRoutesNavigator

class MyRoutesFragment : BaseFragment(R.layout.fragment_my_routes){
    private var _binding: FragmentMyRoutesBinding? = null
    private val binding get() = _binding!!
    private val myRoutesRecyclerAdapter: MyRoutesRecyclerAdapter by inject()
    private val divider: DividerItemDecoration by inject()
    private val myRoutesNavigator: MyRoutesNavigator by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyRoutesBinding.bind(view)
        initRecycler()
    }

    override fun onResume() {
        super.onResume()
        initObservers()
        initRecycler()
    }

    override fun initObservers() {
        super.initObservers()
        observeMyRoutes()
    }

    override fun initViews() {
        super.initViews()
    }

    private fun observeMyRoutes() {
        viewModel.myRoutes.observe(viewLifecycleOwner) {
            myRoutesRecyclerAdapter.setItems(it)
        }
    }

    private fun initRecycler() {
        with(binding.recyclerView) {
            addItemDecoration(divider)
            setHasFixedSize(true)
            myRoutesRecyclerAdapter.onItemClick = { route ->
                myRoutesNavigator.openFollowRouteFragment(route)
            }
            adapter = myRoutesRecyclerAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyRecycler() // needed for every Recycler View Adapter while using Navigation Component
    }

    private fun destroyRecycler() {
        binding.recyclerView.layoutManager = null
        binding.recyclerView.adapter = null
    }

    companion object {
        val LOG_TAG = MyRoutesFragment::class.simpleName
    }
}