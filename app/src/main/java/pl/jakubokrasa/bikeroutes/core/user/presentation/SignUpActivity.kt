package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jakubokrasa.bikeroutes.MainActivity
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserUseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.UserRepository
import pl.jakubokrasa.bikeroutes.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.signupBtn.setOnClickListener(btSignUpOnClick)
        binding.loginBtn.setOnClickListener(btLogInOnClick)

        observeMessage()
    }

    private fun observeMessage() {
        userViewModel.message.observe(this, {
            if(it.first) startActivity(Intent(this, MainActivity::class.java))
            showToast(it.second)
        })
    }

    private fun showToast(message: String?) {
        message?.let { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
    }

    private val btSignUpOnClick = View.OnClickListener {
        var email: String = binding.emailEt.text.toString()
        var password: String = binding.passwordEt.text.toString()

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_LONG).show()
        } else if (password.length<6) {
            Toast.makeText(this, "Password must have at least 6 characters.", Toast.LENGTH_LONG).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email address is invalid.", Toast.LENGTH_LONG).show()
        } else{
            userViewModel.createUser(email, password)
        }
    }

    private val btLogInOnClick = View.OnClickListener {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}