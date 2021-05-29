package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseActivity
import pl.jakubokrasa.bikeroutes.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity: BaseActivity<ForgotPasswordViewModel>() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override val viewModel: ForgotPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.resetPassBtn.setOnClickListener {
            val email :String = binding.etEmail.text.toString()
            if(TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_LONG).show()
            }
            else {
                viewModel.resetPassword(email)
            }
        }
    }
}