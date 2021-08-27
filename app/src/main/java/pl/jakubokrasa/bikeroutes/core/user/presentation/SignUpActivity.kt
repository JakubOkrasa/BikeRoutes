package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jakubokrasa.bikeroutes.core.app.presentation.MainActivity
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseActivity
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity<SignUpViewModel>() {
    private lateinit var binding: ActivitySignUpBinding
    override val viewModel: SignUpViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btSignUp.setOnClickListener(btSignUpOnClick)
        binding.btSignIn.setOnClickListener(btLogInOnClick)

        viewModel.message.observe(this, {
            showToast(it)
        })

        observeStartActivity()
    }

    private fun observeStartActivity() {
        viewModel.startActivity.observe(this, {
            if (it) startActivity(Intent(this, MainActivity::class.java))
        })
    }

    private val btSignUpOnClick = View.OnClickListener {
        val email: String = binding.etEmail.text.toString()
        val password: String = binding.etPassword.text.toString()
        val displayName: String = binding.etDisplayName.text.toString()

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(displayName) ) {
            showToast("Please fill all the fields!")
        } else if (password.length<6) {
            showToast("Password need to have at least 6 characters.")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Email address is invalid.")
        } else if (displayName.length<3) {
            showToast("Name need to have at least 3 characters.")
        } else if(!displayName.matches("[a-zA-Z0-9-]{3,15}".toRegex())) {
            showToast("Display Name can contain only letters, digits an dashes and have from 3 to 15 characters.")
        } else{
            viewModel.createUser(email, password, displayName)
        }
    }

    private val btLogInOnClick = View.OnClickListener {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
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