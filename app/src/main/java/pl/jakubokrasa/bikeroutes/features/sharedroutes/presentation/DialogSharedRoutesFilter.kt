package pl.jakubokrasa.bikeroutes.features.sharedroutes.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import com.google.android.material.slider.RangeSlider
import pl.jakubokrasa.bikeroutes.core.extensions.*
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterDistance
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterDistanceGreaterThan
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterDistanceLessThan
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterLocation
import pl.jakubokrasa.bikeroutes.databinding.DialogSharedroutesFilterBinding
import pl.jakubokrasa.bikeroutes.databinding.FragmentSharedRoutesBinding
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.FilterData
import pl.jakubokrasa.bikeroutes.features.common.filter.presentation.model.GeocodingItemDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO

//very similar to DialogMyRoutesFilter class but creating base class was impossible because of ViewBinding
class DialogSharedRoutesFilter(
    ctx: Context,
    private val frgBinding: FragmentSharedRoutesBinding,
    private val viewModel: SharedRoutesViewModel
): Dialog(ctx) {
    private lateinit var dlgBinding: DialogSharedroutesFilterBinding
    private var previousValFrom = 0
    private var previousValTo = DISTANCE_SLIDER_VALUE_TO.toInt()
    private var previousLocation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        dlgBinding = DialogSharedroutesFilterBinding.inflate(LayoutInflater.from(context))
        setContentView(dlgBinding.root)
        initializeDistanceSlider()

        dlgBinding.btSave.setOnClickListener(btSaveOnClick)
        dlgBinding.btCancel.setOnClickListener(btCancelOnClick)
        dlgBinding.btReset.setOnClickListener(btResetOnClick)
    }

    private fun resetFilterValues() {
        dlgBinding.sliderDistance.setValues(0.0F, DISTANCE_SLIDER_VALUE_TO)
        dlgBinding.etLocation.text.clear()
    }

    override fun onStart() {
        super.onStart()
        previousValFrom = dlgBinding.sliderDistance.getValFrom()
        previousValTo = dlgBinding.sliderDistance.getValTo()
        previousLocation = dlgBinding.etLocation.text.toString()
    }

    private fun isDistanceChanged()
            = previousValFrom != dlgBinding.sliderDistance.getValFrom() || previousValTo != dlgBinding.sliderDistance.getValTo()

    private fun isLocationChanged() = previousLocation != dlgBinding.etLocation.text.toString()

    private fun showFilterInfoInFragment() { //todo this method should be in fragment
        showDistanceInfoInFragment()
    }

    private fun showDistanceInfoInFragment() {
        if (dlgBinding.sliderDistance.getValFrom() > 0.0f) {
            frgBinding.btFilterDistgreaterthan.text = getFormattedFilterDistanceGreaterThan(dlgBinding.sliderDistance.getValFrom())
            frgBinding.btFilterDistgreaterthan.makeVisible()
        } else {
            frgBinding.btFilterDistgreaterthan.makeGone()
        }
        if (dlgBinding.sliderDistance.getValTo() < DISTANCE_SLIDER_VALUE_TO) {
            frgBinding.btFilterDistlessthan.text = getFormattedFilterDistanceLessThan(dlgBinding.sliderDistance.getValTo())
            frgBinding.btFilterDistlessthan.makeVisible()
        } else {
            frgBinding.btFilterDistlessthan.makeGone()
        }
    }

    private fun showLocationInfoInFragment(displayName: String) {
        if(dlgBinding.etLocation.text.isNotEmpty()) {
            frgBinding.btFilterLocation.text = getFormattedFilterLocation(displayName)
            frgBinding.btFilterLocation.makeVisible()
        } else {
            frgBinding.btFilterLocation.makeGone()
        }
    }

    private fun initializeDistanceSlider() {
        with(dlgBinding.sliderDistance) {
            valueFrom = 0.0f
            valueTo = DISTANCE_SLIDER_VALUE_TO
            setValues(0.0f, DISTANCE_SLIDER_VALUE_TO)
            addOnChangeListener(RangeSlider.OnChangeListener { _, _, _ ->
                dlgBinding.tvDistanceResult.text = getFormattedFilterDistance(getValFrom(), getValTo())
            })
        }

    }

    fun completeFilterSave(geocodingItemDisplayable: GeocodingItemDisplayable) {
        viewModel.getSharedRoutesWithFilter(FilterData(dlgBinding.sliderDistance.getValFrom(), dlgBinding.sliderDistance.getValTo(), geocodingItemDisplayable.boundingBox))
        showLocationInfoInFragment(geocodingItemDisplayable.displayName)
    }

    private val btSaveOnClick = View.OnClickListener {
        if(dlgBinding.etLocation.text.length == 1 || dlgBinding.etLocation.text.length == 2) {
            showToast("Location must contain at least 3 characters")
            return@OnClickListener
        }

        val filterData = FilterData()
        if(isDistanceChanged()) {
            filterData.minDistanceKm = dlgBinding.sliderDistance.getValFrom()
            filterData.maxDistanceKm = dlgBinding.sliderDistance.getValTo()
        }
        if(isLocationChanged()) {
            if(dlgBinding.etLocation.text.isBlank())
                frgBinding.btFilterLocation.makeGone()
            else
                viewModel.getGeocodingItem(dlgBinding.etLocation.text.toString())
        }
        viewModel.getSharedRoutesWithFilter(FilterData(dlgBinding.sliderDistance.getValFrom(), dlgBinding.sliderDistance.getValTo()))
        showFilterInfoInFragment()
        dismiss()
    }

    private val btCancelOnClick = View.OnClickListener { dismiss() }
    private val btResetOnClick = View.OnClickListener { resetFilterValues() }
}