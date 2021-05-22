package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.hideKeyboard
import pl.jakubokrasa.bikeroutes.core.user.navigation.UserNavigator
import pl.jakubokrasa.bikeroutes.databinding.FragmentSignInBinding

class SignInFragment : BaseFragment<UserViewModel>(R.layout.fragment_sign_in) {
    override val viewModel: UserViewModel by sharedViewModel()
    private val auth: FirebaseAuth by inject()
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val userNavigator: UserNavigator by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSignInBinding.bind(view)

        binding.btSignIn.setOnClickListener(btSignInOnclick)
        binding.btSignUp.setOnClickListener(btSignUpOnClick)
        binding.tvForgotPassword.setOnClickListener(btForgotPasswordOnClick)
    }

    private val btSignInOnclick = View.OnClickListener {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showToast("Please fill all the fields")
        } else{
            hideKeyboard()
            viewModel.signIn(email, password)
            auth.signInWithEmailAndPassword(email, password)
        }
    }

    private val btSignUpOnClick = View.OnClickListener {
        userNavigator.signInToSignUp()
    }

    private val btForgotPasswordOnClick = View.OnClickListener {
        userNavigator.signInToForgotPassword()
    }
}