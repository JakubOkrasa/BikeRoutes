package pl.jakubokrasa.bikeroutes.core.app.presentation

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_EMAIL
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_PASSWORD
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val preferenceHelper: PreferenceHelper by inject()
    private val viewModel: MainViewModel by viewModel()
    private val mainNavigator: MainNavigator by inject()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        signInIfAnonymous()
        observeIsSignedIn()
    }

    private fun initViews() {
        setContentView(binding.root)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    private fun observeIsSignedIn() {
        viewModel.isSignedIn.observe(this, {
            if(it) {
                initViews()
//                binding.imageWelcome.makeGone()
//                binding.navView.makeVisible()
//                binding.navHostFragment.makeVisible()
            }
        })
    }

    private fun signInIfAnonymous() {
        if (viewModel.isUserSignedIn()) {
            initViews()
        }
        else {
            val email = preferenceHelper.preferences.getString(PREF_KEY_USER_EMAIL, "")
            val password = preferenceHelper.preferences.getString(PREF_KEY_USER_PASSWORD, "")
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                binding.imageWelcome.makeGone()
//                binding.navHostFragment.makeVisible()
//                mainNavigator.navigateTo(R.layout.fragment_sign_up)
                viewModel.signIn("kubao.fb@gmail.com", "111111")
            }
            else
                viewModel.signIn(email!!, password!!)
            }
        }

}