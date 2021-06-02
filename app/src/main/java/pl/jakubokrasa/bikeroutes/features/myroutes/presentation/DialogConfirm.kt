package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.databinding.DialogConfirmBinding

class DialogConfirm(
    ctx: Context,
    private val title: String,
    private val btConfirmLabel: String,
): Dialog(ctx) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        val binding = DialogConfirmBinding
            .inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        binding.tvTitle.text = title
        binding.btConfirm.text = btConfirmLabel
        binding.btConfirm.setOnClickListener(btConfirmOnClick)
    }

    private val btConfirmOnClick = View.OnClickListener {

    }
}