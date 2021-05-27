package pl.jakubokrasa.bikeroutes.core.app.presentation

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
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

    private fun initNavigation() {
        setContentView(binding.root)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    private fun observeIsSignedIn() {
        viewModel.isSignedIn.observe(this, {
            if(it) {
                homeDestination = R.id.nav_map
                initNavigation()
//                binding.imageWelcome.makeGone()
//                binding.navView.makeVisible()
//                binding.navHostFragment.makeVisible()
            } else {
                homeDestination = R.id.signUpFragment
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            mainNavigator.navigateTo(R.id.action_nav_map_to_signUpFragment)
        }
        return true
    }

    private fun signInIfAnonymous() {
        if (viewModel.isUserSignedIn()) {
            initNavigation()
        }
        else {
            val email = preferenceHelper.preferences.getString(PREF_KEY_USER_EMAIL, "")
            val password = preferenceHelper.preferences.getString(PREF_KEY_USER_PASSWORD, "")
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//                binding.imageWelcome.makeGone()
//                binding.navHostFragment.makeVisible()
                initNavigation()
                mainNavigator.navigateTo(R.id.action_nav_map_to_signUpFragment)
//                viewModel.signIn("kubao.fb@gmail.com", "111111")
            }
            else
                viewModel.signIn(email!!, password!!)
            }
        }

    companion object {
        var homeDestination = R.id.signUpFragment
    }

}