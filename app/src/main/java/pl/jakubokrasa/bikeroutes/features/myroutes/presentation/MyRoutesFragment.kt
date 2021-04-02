package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.FragmentMyRoutesBinding
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteViewModel

class MyRoutesFragment : Fragment(R.layout.fragment_my_routes){
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

    override fun onResume() {
        super.onResume()
        initObservers()
        initRecycler()
    }

    private fun initObservers() {
        observeMyRoutes()
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

                childFragmentManager.commit {
                    add<FollowRouteFragment>(binding.frgContainer.id)
                    setReorderingAllowed(true)
                    addToBackStack("Follow route")
                    childFragmentManager.setFragmentResult("requestKey", bundleOf("route" to route))
//                    arguments = Bundle() .also { it.putSerializable("route", route) }
//                    arguments = bundleOf(Pair("route", route))


                }
            }
            adapter = myRoutesRecyclerAdapter
        }
    }

    companion object {
        val LOG_TAG = MyRoutesFragment::class.simpleName
    }
}