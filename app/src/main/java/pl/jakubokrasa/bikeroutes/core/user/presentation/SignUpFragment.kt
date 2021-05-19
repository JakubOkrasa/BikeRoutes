package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.FragmentSignUpBinding

class SignUpFragment : BaseFragment<UserViewModel>(R.layout.fragment_sign_up) {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    override val viewModel: UserViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignUpBinding.bind(view)

        binding.btSignUp.setOnClickListener(btSignUpOnClick)
        binding.btSignIn.setOnClickListener(btSignInOnClick)

    }

    private val btSignUpOnClick = View.OnClickListener {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showToast("Please fill all the fields!")
        } else if (password.length < 6) {
            showToast("Password must have at least 6 characters.")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Email address is invalid.")
        } else{
            viewModel.createUser(email, password)
        }
    }

    private val btSignInOnClick = View.OnClickListener {
        viewModel.navFromSignUpToSignIn()
    }
}