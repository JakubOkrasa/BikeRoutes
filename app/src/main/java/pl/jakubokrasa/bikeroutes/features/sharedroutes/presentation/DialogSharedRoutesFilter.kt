package pl.jakubokrasa.bikeroutes.features.sharedroutes.presentation

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
import pl.jakubokrasa.bikeroutes.databinding.DialogSharedroutesFilterBinding
import pl.jakubokrasa.bikeroutes.databinding.FragmentSharedRoutesBinding
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO
import pl.jakubokrasa.bikeroutes.features.sharedroutes.presentation.SharedRoutesViewModel

class DialogSharedRoutesFilter(
    ctx: Context,
    private val frgBinding: FragmentSharedRoutesBinding,
    private val viewModel: SharedRoutesViewModel
): Dialog(ctx) {
    private lateinit var dlgBinding: DialogSharedroutesFilterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        dlgBinding = DialogSharedroutesFilterBinding.inflate(LayoutInflater.from(context))
        setContentView(dlgBinding.root)
        initializeDistanceSlider()
        dlgBinding.btSave.setOnClickListener {
            viewModel.getSharedRoutesWithFilter(FilterData(dlgBinding.sliderDistance.getValFrom(), dlgBinding.sliderDistance.getValTo()))

            if(dlgBinding.sliderDistance.getValFrom() > 0.0f) {
                frgBinding.btFilterDistgreaterthan.text =
                    getFormattedFilterDistanceGreaterThan(dlgBinding.sliderDistance.getValFrom())
                frgBinding.btFilterDistgreaterthan.makeVisible()
            } else {
                frgBinding.btFilterDistgreaterthan.makeGone()
            }
            if(dlgBinding.sliderDistance.getValTo() < DISTANCE_SLIDER_VALUE_TO) {
                frgBinding.btFilterDistlessthan.text =
                    getFormattedFilterDistanceLessThan(dlgBinding.sliderDistance.getValTo())
                frgBinding.btFilterDistlessthan.makeVisible()
            } else {
                frgBinding.btFilterDistlessthan.makeGone()
            }

            dismiss()
        }
        dlgBinding.btCancel.setOnClickListener { dismiss() }
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
}