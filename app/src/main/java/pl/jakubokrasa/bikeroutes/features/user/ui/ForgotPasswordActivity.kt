package pl.jakubokrasa.bikeroutes.features.user.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.ActivityForgotPasswordBinding
import pl.jakubokrasa.bikeroutes.databinding.ActivityLoginBinding
import pl.jakubokrasa.bikeroutes.databinding.ActivitySignUpBinding

class ForgotPasswordActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by inject()
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.resetPassBtn.setOnClickListener {
            val email :String = binding.emailEdtText.text.toString()
            if(TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter your email", Toast.LENGTH_LONG).show()
            }
            else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Reset password message was sent.", Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(this, "Error: Reset password message wasn't sent.", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}