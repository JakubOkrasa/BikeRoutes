package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import pl.jakubokrasa.bikeroutes.databinding.DialogSegmentBinding

class DialogSegment(
    ctx: Context,
    private val viewModel: MyRoutesViewModel,
): Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        val binding = DialogSegmentBinding
            .inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        binding.btSave.setOnClickListener(btSaveOnClick)
        binding.btCancel.setOnClickListener(btCancelOnClick)
    }

    private val btSaveOnClick = View.OnClickListener {
//        viewModel.saveSegment()
        dismiss()
    }

    private val btCancelOnClick = View.OnClickListener {
        dismiss()
    }
}