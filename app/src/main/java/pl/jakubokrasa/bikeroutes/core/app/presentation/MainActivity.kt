package pl.jakubokrasa.bikeroutes.core.app.presentation

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseActivity
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_EMAIL
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_PASSWORD
import pl.jakubokrasa.bikeroutes.core.user.presentation.SignInActivity
import pl.jakubokrasa.bikeroutes.core.user.presentation.SignUpActivity
import pl.jakubokrasa.bikeroutes.databinding.ActivityMainBinding


class MainActivity : BaseActivity<MainViewModel>() {
    private val preferenceHelper: PreferenceHelper by inject()
    override val viewModel: MainViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        viewModel.isSignedIn.observe(this, {
            if(it) initViews()
        })

        viewModel.startActivity.observe(this, {
            if(it) startActivity(Intent(this, SignInActivity::class.java))
        })

        viewModel.message.observe(this, {
            showToast(it)
        })

        signInIfAnonymous()
    }

    private fun initViews() {
        setContentView(binding.root)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }

    private fun signInIfAnonymous() {
        if (viewModel.isUserSignedIn()) {
            initViews()
        } else {
            val userEmail = preferenceHelper.preferences.getString(PREF_KEY_USER_EMAIL, "")
            val userPassword = preferenceHelper.preferences.getString(PREF_KEY_USER_PASSWORD, "")
            if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) startActivity(
                Intent(this, SignUpActivity::class.java))
            else
                viewModel.signIn(userEmail!!, userPassword!!)
        }
    }
}