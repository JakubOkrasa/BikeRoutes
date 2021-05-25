package pl.jakubokrasa.bikeroutes.core.app.presentation

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_EMAIL
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_PASSWORD
import pl.jakubokrasa.bikeroutes.core.user.presentation.SignInFragment
import pl.jakubokrasa.bikeroutes.core.user.presentation.SignUpFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {
    private val preferenceHelper: PreferenceHelper by inject()
    private val viewModel: MainViewModel by viewModel()
    private val mainNavigator: MainNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInIfAnonymous()
        observeIsSignedIn()
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

    private fun observeIsSignedIn() {
        viewModel.isSignedIn.observe(this, {
            if(it) initViews() //todo test it
        })
    }

    private fun signInIfAnonymous() {
        if (viewModel.isUserSignedIn()) {
            initViews()
        }
        else {
            val email = preferenceHelper.preferences.getString(PREF_KEY_USER_EMAIL, "")
            val password = preferenceHelper.preferences.getString(PREF_KEY_USER_PASSWORD, "")
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
                mainNavigator.navigateTo(R.layout.fragment_sign_up)
            else
                viewModel.signIn(email!!, password!!)
            }
        }

}