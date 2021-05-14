package pl.jakubokrasa.bikeroutes

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_EMAIL
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_PASSWORD
import pl.jakubokrasa.bikeroutes.core.user.presentation.LoginActivity
import pl.jakubokrasa.bikeroutes.core.user.presentation.SignUpActivity

class MainActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by inject()
    private val preferenceHelper: PreferenceHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInIfAnonymous()

        Log.d("MainActivity", "logged as " + auth.currentUser?.email.toString())
    }

    private fun initViews() {
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        //        val appBarConfiguration = AppBarConfiguration(setOf(
        //                R.id.nav_record_route, R.id.nav_my_routes, R.id.navigation_notifications))
        //        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun signInIfAnonymous() {
        if (auth.currentUser == null) {
            val userEmail = preferenceHelper.preferences.getString(PREF_KEY_USER_EMAIL, "")
            val userPassword = preferenceHelper.preferences.getString(PREF_KEY_USER_PASSWORD, "")
            if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) startActivity(
                Intent(this, SignUpActivity::class.java))
            else auth.signInWithEmailAndPassword(userEmail!!, userPassword!!).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        initViews()
                    }
                    else {
                        Toast.makeText(this@MainActivity, "sign in error!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }
        }
        else {
            initViews()
        }
    }


}