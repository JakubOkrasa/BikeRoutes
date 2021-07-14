package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import pl.jakubokrasa.bikeroutes.R.color
import pl.jakubokrasa.bikeroutes.databinding.DialogSegmentBinding
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.SegmentType
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.SegmentBasicModel
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model.SegmentDisplayable

class DialogSegment(
    ctx: Context,
    private val viewModel: MyRoutesViewModel,
    private val segmentBasicModel: SegmentBasicModel
): Dialog(ctx) {
    private lateinit var binding: DialogSegmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        binding = DialogSegmentBinding
            .inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        binding.btSave.setOnClickListener(btSaveOnClick)
        binding.btCancel.setOnClickListener(btCancelOnClick)

        initSpinner()
        binding.segColorRed.isChecked = true
    }

    private fun initSpinner() {
        val spinnerAdapter = ArrayAdapter(context,
            R.layout.simple_dropdown_item_1line,
            SegmentType.values())
        binding.spinner.adapter = spinnerAdapter
    }

    private val btSaveOnClick = View.OnClickListener {
        val segmentDisplayable = SegmentDisplayable(
            "",
            segmentBasicModel.routeId,
            segmentBasicModel.beginIndex,
            segmentBasicModel.endIndex,
            binding.spinner.selectedItem as SegmentType,
            binding.etAdditionalInfo.text.toString(),
            getCheckedColor()
        )

        viewModel.addSegment(segmentDisplayable)
        dismiss()
    }

    @SuppressLint("ResourceType")
    private fun getCheckedColor(): String {
        val segmentColor: String
        with(binding) {
            segmentColor = when {
                segColorRed.isChecked -> context.resources.getString(color.seg_red)
                segColorOrange.isChecked -> context.resources.getString(color.seg_orange)
                segColorYellow.isChecked -> context.resources.getString(color.seg_yellow)
                segColorGreen.isChecked -> context.resources.getString(color.seg_light_green)
                segColorBlue.isChecked -> context.resources.getString(color.seg_light_blue)
                else -> context.resources.getString(color.seg_red)
            }
        }
        return segmentColor
    }

    private val btCancelOnClick = View.OnClickListener {
        dismiss()
    }
}