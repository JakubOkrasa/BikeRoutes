package pl.jakubokrasa.bikeroutes.core.util

import android.content.SharedPreferences
import android.graphics.Color
import org.koin.java.KoinJavaComponent.inject
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.di.preferencesModule
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_EMAIL
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_PASSWORD

class AppUtil(private val preferenceHelper: PreferenceHelper) {


    fun getHomeDestination(): Int {
        if(preferenceHelper.preferences.contains(PREF_KEY_USER_EMAIL)
            && preferenceHelper.preferences.contains(PREF_KEY_USER_PASSWORD))  {
            return R.id.nav_map
        } else {
            return R.id.signUpFragment
        }
    }
}
