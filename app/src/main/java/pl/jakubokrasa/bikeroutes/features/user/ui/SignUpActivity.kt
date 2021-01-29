package pl.jakubokrasa.bikeroutes.features.user.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.MainActivity
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val binding = ActivitySignUpBinding.inflate(layoutInflater)

        binding.signupBtn.setOnClickListener{
            var email: String = binding.emailEt.text.toString()
            var password: String = binding.passwordEt.text.toString()

            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show()
            } else{
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else {
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        binding.loginBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}