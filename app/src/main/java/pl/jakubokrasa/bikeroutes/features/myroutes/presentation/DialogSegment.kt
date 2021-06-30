package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.R
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import androidx.annotation.NonNull
import kotlinx.android.synthetic.main.dialog_segment.*
import pl.jakubokrasa.bikeroutes.databinding.DialogMyroutesFilterBinding
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
            binding.etAdditionalInfo.text.toString()
        )
        viewModel.addSegment(segmentDisplayable)
        dismiss()
    }

    private val btCancelOnClick = View.OnClickListener {
        dismiss()
    }
}