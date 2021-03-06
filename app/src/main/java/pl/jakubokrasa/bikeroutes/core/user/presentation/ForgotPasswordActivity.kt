package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseActivity
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity: BaseActivity<ForgotPasswordViewModel>() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override val viewModel: ForgotPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.message.observe(this, { showToast(it) })

        binding.backBtn.setOnClickListener { finish() }
        binding.resetPassBtn.setOnClickListener(btResetOnClick)
    }

    private val btResetOnClick = View.OnClickListener {
        val email :String = binding.etEmail.text.toString().trim()
        if(TextUtils.isEmpty(email)) {
            showToast("Enter your email")
        }
        else {
            viewModel.resetPassword(email)
        }
    }

    override fun onPendingState() {
        super.onPendingState()
        binding.progressLayout.makeVisible()
    }

    override fun onIdleState() {
        super.onIdleState()
        binding.progressLayout.makeGone()
    }
}