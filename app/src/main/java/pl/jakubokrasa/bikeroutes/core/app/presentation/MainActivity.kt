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
import pl.jakubokrasa.bikeroutes.core.util.AppUtil
import pl.jakubokrasa.bikeroutes.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val preferenceHelper: PreferenceHelper by inject()
    private val viewModel: MainViewModel by viewModel()
    private val appUtil: AppUtil by inject()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

        observeIsSignedIn()
        autoSignInIfPossible()
    }

    private fun initNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)
        graph.startDestination = appUtil.getHomeDestination()

        navController.graph = graph

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setupWithNavController(navController)
    }

    fun observeIsSignedIn() {
        viewModel.isSignedIn.observe(this, {
            if (it) {
//                binding.imageWelcome.makeGone()
                binding.navView.makeVisible()
//                binding.hostFragmentContainer.makeVisible()
            } else {
//                binding.imageWelcome.makeVisible()
                binding.navView.makeGone()
//                binding.hostFragmentContainer.makeGone()
            }
        })
    }

    private fun autoSignInIfPossible() {
        val email = preferenceHelper.preferences.getString(PREF_KEY_USER_EMAIL, "")
        val password = preferenceHelper.preferences.getString(PREF_KEY_USER_PASSWORD, "")
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            viewModel.signIn(email!!, password!!)
        }
    }
}