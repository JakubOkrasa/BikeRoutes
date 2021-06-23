package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.google.android.material.slider.RangeSlider
import pl.jakubokrasa.bikeroutes.core.extensions.getValFrom
import pl.jakubokrasa.bikeroutes.core.extensions.getValTo
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterDistance
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterDistanceGreaterThan
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterDistanceLessThan
import pl.jakubokrasa.bikeroutes.core.util.getFormattedFilterLocation
import pl.jakubokrasa.bikeroutes.databinding.DialogMyroutesFilterBinding
import pl.jakubokrasa.bikeroutes.databinding.FragmentMyRoutesBinding
import pl.jakubokrasa.bikeroutes.features.common.domain.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.GeocodingItemDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO
import java.util.*

class DialogMyRoutesFilter(
    ctx: Context,
    private val frgBinding: FragmentMyRoutesBinding,
    private val viewModel: MyRoutesViewModel
): Dialog(ctx) {
    private lateinit var dlgBinding: DialogMyroutesFilterBinding
    private var previousValFrom = 0
    private var previousValTo = DISTANCE_SLIDER_VALUE_TO.toInt()
    private var previousLocation = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        dlgBinding = DialogMyroutesFilterBinding.inflate(LayoutInflater.from(context))
        setContentView(dlgBinding.root)
        initializeDistanceSlider()

        dlgBinding.btSave.setOnClickListener {

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

                viewModel.getMyRoutesWithFilter(FilterData(dlgBinding.sliderDistance.getValFrom(), dlgBinding.sliderDistance.getValTo()))

            showFilterInfoInFragment()

            dismiss()
        }
        dlgBinding.btCancel.setOnClickListener { dismiss() }
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
        viewModel.getMyRoutesWithFilter(FilterData(dlgBinding.sliderDistance.getValFrom(), dlgBinding.sliderDistance.getValTo(), geocodingItemDisplayable.boundingBox))
        showLocationInfoInFragment(geocodingItemDisplayable.displayName)
    }
}