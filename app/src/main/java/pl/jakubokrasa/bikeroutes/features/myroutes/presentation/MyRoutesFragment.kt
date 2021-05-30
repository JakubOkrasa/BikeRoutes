 package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.google.android.material.slider.RangeSlider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.getValFrom
import pl.jakubokrasa.bikeroutes.core.extensions.getValTo
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterDistance
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterDistanceGreaterThan
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterDistanceLessThan
import pl.jakubokrasa.bikeroutes.databinding.FragmentMyRoutesBinding
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable

 class MyRoutesFragment : BaseFragment<MyRoutesViewModel>(R.layout.fragment_my_routes){
    private var _binding: FragmentMyRoutesBinding? = null
    private val binding get() = _binding!!
    private val myRoutesRecyclerAdapter: MyRoutesRecyclerAdapter by inject()
	override val viewModel: MyRoutesViewModel by sharedViewModel()
    private lateinit var dialogFilter: Dialog
    private var isFilter = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMyRoutesBinding.bind(view)
        initRecycler() // todo ten init powinien być w initViews() ale wtedy jest java.lang.NullPointerException
                        //at pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.getBinding(MyRoutesFragment.kt:17)
                        //w AA to działą
        viewModel.getMyRoutes()

        binding.btFilter.setOnClickListener(btFilterOnClick)
        dialogFilter = Dialog(requireContext())
        initializeFilterDialog(dialogFilter)
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
    }

    override fun initViews() {
        super.initViews()
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

     private fun showNoDataMessage() {
         if (isFilter) binding.tvNoData.text = String.format(getString(R.string.fragment_common_no_data_filter))
         else binding.tvNoData.text = getString(R.string.fragment_myroutes_no_data)
         binding.tvNoData.visibility = View.VISIBLE
         binding.recyclerView.visibility = View.GONE
     }

     private fun showRecyclerWithItems(it: List<RouteDisplayable>) {
         binding.recyclerView.visibility = View.VISIBLE
         binding.tvNoData.visibility = View.GONE
         myRoutesRecyclerAdapter.setItems(it)
     }

     private fun observePoints() {
         viewModel.pointsFromRemote.observe(viewLifecycleOwner) {
             // not needed right now, points are taken from remote onClick item by viewModel
         }
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

     private val btFilterOnClick = View.OnClickListener {
         dialogFilter.show()
     }

     private fun initializeFilterDialog(dialog: Dialog): DialogBinder {
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
         dialog.setCancelable(true)
         dialog.setContentView(R.layout.dialog_myroutes_filter)
         val btSave = dialog.findViewById<Button>(R.id.dialog_myroutesfilter_bt_save) //todo view binding like in DialogConfirm
         val btCancel = dialog.findViewById<Button>(R.id.dialog_myroutesfilter_bt_cancel)
         val slider = dialog.findViewById<RangeSlider>(R.id.dialog_myroutesfilter_slider_distance)
         val tvResult = dialog.findViewById<TextView>(R.id.dialog_myroutesfilter_tv_distance_result)
         val dialogBinder = DialogBinder(slider, btSave, btCancel, tvResult)
         initializeDistanceSlider(dialogBinder)
         dialogBinder.btSave.setOnClickListener {
             viewModel.getMyRoutesWithFilter(FilterData(dialogBinder.slider.getValFrom(), dialogBinder.slider.getValTo()))

             if(dialogBinder.slider.getValFrom() > 0.0f) {
                 binding.btFilterDistgreaterthan.text =
                     getFormattedFilterDistanceGreaterThan(dialogBinder.slider.getValFrom())
                 binding.btFilterDistgreaterthan.makeVisible()
             } else {
                 binding.btFilterDistgreaterthan.makeGone()
             }
             if(dialogBinder.slider.getValTo() < DISTANCE_SLIDER_VALUE_TO) {
                 binding.btFilterDistlessthan.text =
                     getFormattedFilterDistanceLessThan(dialogBinder.slider.getValTo())
                 binding.btFilterDistlessthan.makeVisible()
             } else {
                 binding.btFilterDistlessthan.makeGone()
             }

             dialog.dismiss()
         }
         dialogBinder.btCancel.setOnClickListener { dialog.dismiss() }
         return dialogBinder
     }

     private fun initializeDistanceSlider(dialogBinder: DialogBinder) {
         with(dialogBinder.slider) {
             valueFrom = 0.0f
             valueTo = DISTANCE_SLIDER_VALUE_TO
             setValues(0.0f, DISTANCE_SLIDER_VALUE_TO)
             addOnChangeListener(RangeSlider.OnChangeListener { _, _, _ ->
                 dialogBinder.tvResult.text = getFormattedFilterDistance(getValFrom(), getValTo())
             })
         }

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
        const val DISTANCE_SLIDER_VALUE_TO = 500.0f
    }

     data class DialogBinder(
         val slider: RangeSlider,
         val btSave: Button,
         val btCancel: Button,
         val tvResult: TextView
     )
}