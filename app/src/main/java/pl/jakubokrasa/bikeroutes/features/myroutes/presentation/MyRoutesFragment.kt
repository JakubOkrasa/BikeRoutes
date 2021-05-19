 package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.FragmentMyRoutesBinding
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.navigation.MyRoutesNavigator
import org.koin.androidx.viewmodel.ext.android.viewModel

 class MyRoutesFragment : BaseFragment<MyRoutesViewModel>(R.layout.fragment_my_routes){
    private var _binding: FragmentMyRoutesBinding? = null
    private val binding get() = _binding!!
    private val myRoutesRecyclerAdapter: MyRoutesRecyclerAdapter by inject()
    override val viewModel: MyRoutesViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyRoutesBinding.bind(view)
        initRecycler() // todo ten init powinien być w initViews() ale wtedy jest java.lang.NullPointerException
                        //at pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.getBinding(MyRoutesFragment.kt:17)
                        //w AA to działą
        viewModel.getMyRoutes()
    }

    override fun onResume() {
        super.onResume()
        initRecycler()
        initObservers()
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
            if(it.isNotEmpty()) {
                binding.tvNoData.visibility = View.GONE
                myRoutesRecyclerAdapter.setItems(it)
            } else
                binding.tvNoData.visibility = View.VISIBLE
        }
    }

     private fun observePoints() {
         viewModel.pointsFromRemote.observe(viewLifecycleOwner) {
             // not needed right now, points are taken from remote onClick item by viewModel
         }
     }

    private fun initRecycler() {
        with(binding.recyclerView) {
            setHasFixedSize(true)
            myRoutesRecyclerAdapter.onItemClick = {
                route -> //todo może od razu przejść do FollowRouteFrg (bez czekania
                        // todo i wtedy user od razu widzi szczegóły trasy)
                        // todo a wynik można przekazać w LiveData
                        // todo ale jeśli potem będzie przechodzenie do odczielnego frg
                        // todo żeby po itemOnclick było widać szczegóły trasy i obrazek (bez follow)
                        // todo to wtedy chcę żeby points się załadowały wcześniej, a potem przechodziło do frg
                viewModel.getPointsFromRemoteAndOpenFollowRouteFrg(route)
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

     override fun onPendingState() {
         super.onPendingState()
         binding.progressLayout.visibility = View.VISIBLE
     }

     override fun onIdleState() {
         super.onIdleState()
         binding.progressLayout.visibility = View.GONE
     }

    companion object {
        val LOG_TAG = MyRoutesFragment::class.simpleName
    }
}