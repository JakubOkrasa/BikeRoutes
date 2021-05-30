package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.core.app.presentation.MainActivity
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseActivity
import pl.jakubokrasa.bikeroutes.databinding.ActivitySignInBinding

class SignInActivity: BaseActivity<SignInViewModel>() {
    private lateinit var binding: ActivitySignInBinding
    override val viewModel: SignInViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.startActivity.observe(this, {
            if(it) startActivity(Intent(this, MainActivity::class.java))
        })

        viewModel.message.observe(this, {
            showToast(it)
        })

        binding.btSignIn.setOnClickListener{
            val email: String = binding.etEmail.text.toString()
            val password: String = binding.etPassword.text.toString()
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@SignInActivity, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else{
                viewModel.signIn(email, password)
            }
        }

        binding.btSignUp.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.tvForgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}