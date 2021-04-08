package pl.jakubokrasa.bikeroutes

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper.Companion.PREF_KEY_USER_EMAIL
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper.Companion.PREF_KEY_USER_PASSWORD
import pl.jakubokrasa.bikeroutes.core.user.ui.LoginActivity

class MainActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by inject() // for debug
    private val preferenceHelper: PreferenceHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.nav_record_route, R.id.nav_my_routes, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        Log.d("MainActivity", "logged as " + auth.currentUser?.email.toString())

        val userEmail = preferenceHelper.preferences.getString(PREF_KEY_USER_EMAIL, "")
//        val userPassword = preferenceHelper.preferences.getString(PREF_KEY_USER_PASSWORD, "")
        if(TextUtils.isEmpty(userEmail)
//            || TextUtils.isEmpty(userPassword)
        ) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }


}