package pl.jakubokrasa.bikeroutes.core.app.presentation

import android.os.Bundle
import android.text.TextUtils
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_EMAIL
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_PASSWORD
import pl.jakubokrasa.bikeroutes.core.util.AppUtil
import pl.jakubokrasa.bikeroutes.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val preferenceHelper: PreferenceHelper by inject()
    private val viewModel: MainViewModel by viewModel()
    private val mainNavigator: MainNavigator by inject()
    private val appUtil: AppUtil by inject()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
        val navController = navHost!!.navController

        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.mobile_navigation)

        graph.startDestination = appUtil.getHomeDestination()

        navController.graph = graph

        signInIfAnonymous()
        observeIsSignedIn()
    }

    private fun initNavigation() {

        val navView: BottomNavigationView = findViewById(R.id.nav_view)


        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    private fun observeIsSignedIn() {
        viewModel.isSignedIn.observe(this, {
            if (it) {
//                homeDestination = R.id.nav_map
                initNavigation()
//                binding.imageWelcome.makeGone()
//                binding.navView.makeVisible()
//                binding.navHostFragment.makeVisible()
            } else {
//                homeDestination = R.id.signUpFragment
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
//                binding.imageWelcome.makeVisible()
//                binding.navView.makeGone()
//                binding.hostFragmentContainer.makeGone()
//                Thread {
//                    Thread.sleep(1000)
//                    binding.imageWelcome.makeGone()
//                    binding.navView.makeVisible()
//                    binding.hostFragmentContainer.makeVisible()
//                }.start()
//                binding.imageWelcome.makeGone()
//                binding.navHostFragment.makeVisible()
                initNavigation()
            }
            else
                viewModel.signIn(email!!, password!!)
            }
        }

    companion object {
    }

}