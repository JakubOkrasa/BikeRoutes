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
import pl.jakubokrasa.bikeroutes.databinding.DialogSegmentBinding
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.SegmentType
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.ColorSpinnerAdapter
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
        initColorSpinner()
    }

    private fun initSpinner() {
        val spinnerAdapter = ArrayAdapter(context,
            R.layout.simple_dropdown_item_1line,
            SegmentType.values())
        binding.spinner.adapter = spinnerAdapter
    }

    private fun initColorSpinner() {
        val colorNames: ArrayList<String>
        for(colorResId: Int in context.resources.getColor(pl.jakubokrasa.bikeroutes.R.array.segment_colors))
            colorNames.add(colorResId)

//        val colorNames = context.resources.getResourceName()e(s)
        colorNames.map { it.name}
        val spinnerAdapter = ColorSpinnerAdapter(context,
            pl.jakubokrasa.bikeroutes.R.layout.color_spinner_item,
            pl.jakubokrasa.bikeroutes.R.array.segment_colors,

            )
        binding.spinnerColor.adapter = spinnerAdapter
    }

    private val btSaveOnClick = View.OnClickListener {
        val segmentDisplayable = SegmentDisplayable(
            "",
            segmentBasicModel.routeId,
            segmentBasicModel.beginIndex,
            segmentBasicModel.endIndex,
            binding.spinner.selectedItem as SegmentType,
            binding.etAdditionalInfo.text.toString()
        )
        viewModel.addSegment(segmentDisplayable)
        dismiss()
    }

    private val btCancelOnClick = View.OnClickListener {
        dismiss()
    }
}