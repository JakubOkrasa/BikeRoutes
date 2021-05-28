package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jakubokrasa.bikeroutes.core.app.presentation.MainActivity
import pl.jakubokrasa.bikeroutes.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btSignUp.setOnClickListener(btSignUpOnClick)
        binding.btSignIn.setOnClickListener(btLogInOnClick)

//        observeMessage()

        viewModel.startActivity.observe(this, {
            if(it) startActivity(Intent(this, MainActivity::class.java))
        })
    }

//    private fun observeMessage() {
//        viewModel.message.observe(this, {
//
//            showToast(it)
//        })
//    }

//    if(it.first) startActivity(Intent(this, MainActivity::class.java))

    private fun showToast(message: String?) {
        message?.let { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
    }

    private val btSignUpOnClick = View.OnClickListener {
        val email: String = binding.etEmail.text.toString()
        val password: String = binding.etPassword.text.toString()

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_LONG).show()
        } else if (password.length<6) {
            Toast.makeText(this, "Password must have at least 6 characters.", Toast.LENGTH_LONG).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email address is invalid.", Toast.LENGTH_LONG).show()
        } else{
            viewModel.createUser(email, password)
        }
    }

    private val btLogInOnClick = View.OnClickListener {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }
}