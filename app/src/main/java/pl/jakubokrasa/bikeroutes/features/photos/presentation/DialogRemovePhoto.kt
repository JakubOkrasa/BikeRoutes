package pl.jakubokrasa.bikeroutes.features.photos.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import pl.jakubokrasa.bikeroutes.databinding.DialogRemovePhotoBinding
import pl.jakubokrasa.bikeroutes.features.photos.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.ui_features.myroutes.presentation.MyRoutesViewModel

class DialogRemovePhoto(
    ctx: Context,
    private val viewModel: MyRoutesViewModel,
    private val photo: PhotoInfoDisplayable,
    private val photoPosition: Int
): Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        val binding = DialogRemovePhotoBinding
            .inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        binding.btConfirm.setOnClickListener(btConfirmOnClick)
        binding.btCancel.setOnClickListener(btCancelOnClick)
    }

    private val btConfirmOnClick = View.OnClickListener {
        viewModel.removePhoto(photo, photoPosition)
        dismiss()
    }

    private val btCancelOnClick = View.OnClickListener {
        dismiss()
    }
}