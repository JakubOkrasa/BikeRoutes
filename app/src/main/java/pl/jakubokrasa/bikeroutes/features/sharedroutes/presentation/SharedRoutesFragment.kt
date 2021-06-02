 package pl.jakubokrasa.bikeroutes.features.sharedroutes.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.databinding.FragmentSharedRoutesBinding
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable

 class SharedRoutesFragment : BaseFragment<SharedRoutesViewModel>(R.layout.fragment_shared_routes){
    private var _binding: FragmentSharedRoutesBinding? = null
    private val binding get() = _binding!!
    private val sharedRoutesRecyclerAdapter: SharedRoutesRecyclerAdapter by inject()
	override val viewModel: SharedRoutesViewModel by sharedViewModel()
    private lateinit var dialogFilter: Dialog
    private var isFilter = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSharedRoutesBinding.bind(view)
        initRecycler() // todo ten init powinien być w initViews() ale wtedy jest java.lang.NullPointerException
                        //at pl.jakubokrasa.bikeroutes.features.myroutes.presentation.SharedRoutesFragment.getBinding(SharedRoutesFragment.kt:17)
                        //w AA to działą
        viewModel.getSharedRoutes()

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
        observeSharedRoutes()
    }

    override fun initViews() {
        super.initViews()
    }

    private fun observeSharedRoutes() {
        viewModel.sharedRoutes.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()) {
                showRecyclerWithItems(it)
            } else {
                showNoDataMessage()
            }
        }
    }

     private fun showNoDataMessage() {
         if (isFilter) binding.tvNoData.text = String.format(getString(R.string.fragment_common_no_data_filter))
         else binding.tvNoData.text = getString(R.string.fragment_sharedroutes_no_data)
         binding.tvNoData.visibility = View.VISIBLE
         binding.recyclerView.visibility = View.GONE
     }

     private fun showRecyclerWithItems(it: List<RouteDisplayable>) {
         binding.recyclerView.visibility = View.VISIBLE
         binding.tvNoData.visibility = View.GONE
         sharedRoutesRecyclerAdapter.setItems(it)
     }
     
     private fun observeIsFilter() {
         viewModel.isFilter.observe(viewLifecycleOwner) {
             isFilter = it
         }
     }

    private fun initRecycler() {
        with(binding.recyclerView) {
            setHasFixedSize(true)
            sharedRoutesRecyclerAdapter.onItemClick = {
                route ->
                viewModel.getPointsFromRemoteAndOpenFollowRouteFrg(route)
            }
            adapter = sharedRoutesRecyclerAdapter
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
         dialogFilter = DialogSharedRoutesFilter(requireContext(), binding, viewModel)
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
        val LOG_TAG = SharedRoutesFragment::class.simpleName
    }
}