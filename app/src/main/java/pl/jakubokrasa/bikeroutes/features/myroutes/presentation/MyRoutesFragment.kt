 package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.databinding.FragmentMyRoutesBinding
import pl.jakubokrasa.bikeroutes.features.common.routes.presentation.model.RouteDisplayable

 class MyRoutesFragment : BaseFragment<MyRoutesViewModel>(R.layout.fragment_my_routes){
    private var _binding: FragmentMyRoutesBinding? = null
    private val binding get() = _binding!!
    private val myRoutesRecyclerAdapter: MyRoutesRecyclerAdapter by inject()
	override val viewModel: MyRoutesViewModel by sharedViewModel()
    private lateinit var dialogFilter: DialogMyRoutesFilter
    private var isFilter = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyRoutesBinding.bind(view)
        initRecycler()
        viewModel.getMyRoutes()

        binding.btFilter.setOnClickListener(btFilterOnClick)
        initializeFilterDialog()
    }

    override fun onResume() {
        super.onResume()
        initRecycler()
        initObservers()
    }

    override fun initObservers() {
        super.initObservers()
        observeIsFilter()
        observeMyRoutes()
        observeGeocodingItem()
    }

    private fun observeMyRoutes() {
        viewModel.myRoutes.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                showRecyclerWithItems(it)
            } else {
                showNoDataMessage()
            }
        }
    }

     private fun observeGeocodingItem() {
        viewModel.geocodingItem.observe(viewLifecycleOwner) {
          dialogFilter.completeFilterSave(it)
          }
     }

     private fun showNoDataMessage() {
         if (isFilter) binding.tvNoData.text = String.format(getString(R.string.fragment_common_no_data_filter))
         else binding.tvNoData.text = getString(R.string.fragment_myroutes_no_data)
         binding.tvNoData.visibility = View.VISIBLE
         binding.recyclerView.visibility = View.GONE
     }

     private fun showRecyclerWithItems(it: List<RouteDisplayable>) {
         binding.recyclerView.makeVisible()
         binding.tvNoData.makeGone()
         myRoutesRecyclerAdapter.setItems(it)
     }

     private fun observeIsFilter() {
         viewModel.isFilter.observe(viewLifecycleOwner) {
             isFilter = it
         }
     }

    private fun initRecycler() {
        with(binding.recyclerView) {
            setHasFixedSize(true)
            myRoutesRecyclerAdapter.onItemClick = {
                route -> viewModel.getPointsFromRemoteAndOpenRouteDetailsFrg(route)
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

     private val btFilterOnClick = View.OnClickListener {
         dialogFilter.show()
     }

     private fun initializeFilterDialog() {
         dialogFilter = DialogMyRoutesFilter(requireContext(), binding, viewModel)
     }

     override fun onPendingState() {
         super.onPendingState()
         binding.progressLayout.makeVisible()
     }

     override fun onIdleState() {
         super.onIdleState()
         binding.progressLayout.makeGone()
     }

    companion object {
        val LOG_TAG = MyRoutesFragment::class.simpleName
        const val DISTANCE_SLIDER_VALUE_TO = 500.0f
    }
}