package pl.jakubokrasa.bikeroutes.core.user.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.MainActivity
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.ActivityLoginBinding
import pl.jakubokrasa.bikeroutes.databinding.FragmentLoginBinding
import pl.jakubokrasa.bikeroutes.databinding.FragmentMapBinding

class LoginFragment : BaseFragment(R.layout.fragment_login) {
    private val auth: FirebaseAuth by inject()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLoginBinding.bind(view)

        binding.loginBtn.setOnClickListener{
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Toast.makeText(context, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        binding.signupBtn.setOnClickListener{
            val intent = Intent(context, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.resetPassTv.setOnClickListener{
            val intent = Intent(context, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }



}