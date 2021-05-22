package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : BaseFragment<UserViewModel>(R.layout.fragment_forgot_password) {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    override val viewModel: UserViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentForgotPasswordBinding.bind(view)

        binding.backBtn.setOnClickListener(btBackOnClick)
        binding.resetPassBtn.setOnClickListener(btResetPasswordOnClick)
    }


    private val btBackOnClick = View.OnClickListener  {
        viewModel.navBack()
    }

    private val btResetPasswordOnClick = View.OnClickListener {
        val email = binding.etEmail.text.toString()
        if(TextUtils.isEmpty(email))
            showToast("Enter your email")
        else
            viewModel.resetPassword(email)
    }
}