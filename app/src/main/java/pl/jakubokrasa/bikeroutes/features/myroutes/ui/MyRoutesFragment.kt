package pl.jakubokrasa.bikeroutes.features.myroutes.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.FragmentMyRoutesBinding
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteViewModel
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.model.RouteWithPointsDisplayable

class MyRoutesFragment : Fragment(R.layout.fragment_my_routes) {
    private val viewModel: RouteViewModel by sharedViewModel() //make shared view model
    private var _binding: FragmentMyRoutesBinding? = null
    private val binding get() = _binding!!
    private val myRoutesRecyclerAdapter: MyRoutesRecyclerAdapter by inject()
    private val divider: DividerItemDecoration by inject()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyRoutesBinding.bind(view)
        initObservers()
        initRecycler()
    }

    fun initObservers() {
        observeMyRoutes()
    }


//    fun dummyData(): ArrayList<RouteWithPointsDisplayable> {
//        var list = ArrayList<RouteWithPointsDisplayable>()
//        list.add(RouteWithPointsDisplayable(false, listOf(Point(100L, GeoPoint(10.32, 5.84)))))
//        list.add(RouteWithPointsDisplayable(false, listOf(Point(101L, GeoPoint(10.32, 5.84)), Point(111L, GeoPoint(10.32, 5.84)))))
//        list.add(RouteWithPointsDisplayable(false, listOf(Point(102L, GeoPoint(10.32, 5.84)), Point(112L, GeoPoint(10.32, 5.84)))))
//        list.add(RouteWithPointsDisplayable(false, listOf(Point(103L, GeoPoint(10.32, 5.84)), Point(113L, GeoPoint(10.32, 5.84)), Point(123L, GeoPoint(10.32, 5.84)))))
//        return list
//        // routeId chyba jednak potrzebne >> a może nie, tu w środku jest listOf
//    }

    private fun observeMyRoutes() {
        viewModel.myRoutes.observe(viewLifecycleOwner) {
            myRoutesRecyclerAdapter.setItems(it)
        }
    }

    private fun initRecycler() {
        with(binding.recyclerView) {
            addItemDecoration(divider)
            setHasFixedSize(true)
            adapter = myRoutesRecyclerAdapter
        }
    }
}