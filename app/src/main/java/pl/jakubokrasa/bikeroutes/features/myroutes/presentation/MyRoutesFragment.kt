 package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.preference.DialogPreference
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.slider.RangeSlider
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.FragmentMyRoutesBinding
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.DataFilterParams
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
        initRecycler() // todo ten init powinien być w initViews() ale wtedy jest java.lang.NullPointerException
                        //at pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.getBinding(MyRoutesFragment.kt:17)
                        //w AA to działą
        viewModel.getMyRoutes(DataFilterParams())

        binding.btFilter.setOnClickListener(btFilterOnClick)
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
            addItemDecoration(divider)
            setHasFixedSize(true)
            myRoutesRecyclerAdapter.onItemClick = {
                route ->
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

     private val btFilterOnClick = View.OnClickListener {
         val dialog = Dialog(requireContext())
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
         dialog.setCancelable(false)
         dialog.setContentView(R.layout.dialog_myroutes_filter)
         val btSave = dialog.findViewById<Button>(R.id.bt_save)
         val btCancel = dialog.findViewById<TextView>(R.id.bt_cancel)
         btSave.setOnClickListener {
             val slider = dialog.findViewById<RangeSlider>(R.id.slider_distance)
             viewModel.getMyRoutes(DataFilterParams(slider.valueFrom.toInt(), slider.valueTo.toInt()))
             dialog.dismiss()
         }
         btCancel.setOnClickListener { dialog.dismiss() }
         dialog.show()
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