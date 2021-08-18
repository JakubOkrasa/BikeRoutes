package pl.jakubokrasa.bikeroutes.core.util

import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_EMAIL
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_PASSWORD
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_UID

class AppUtil(private val preferenceHelper: PreferenceHelper) {


    fun getHomeDestination(): Int {
        if(preferenceHelper.preferences.contains(PREF_KEY_USER_EMAIL)
            && preferenceHelper.preferences.contains(PREF_KEY_USER_PASSWORD)
            && preferenceHelper.preferences.contains(PREF_KEY_USER_UID)){
            return R.id.nav_map
        } else {
            return R.id.signUpFragment
        }
    }
}
