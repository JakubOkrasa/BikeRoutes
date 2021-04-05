package pl.jakubokrasa.bikeroutes.core.util

import android.content.Context
import androidx.core.content.edit
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper.Companion.PREF_KEY_REQUESTING_LOCATION_UPDATES

class LocationUtils(private val preferenceHelper: PreferenceHelper) {
    fun requestingLocationUpdates(context: Context?): Boolean {
        return preferenceHelper.preferences.getBoolean(PREF_KEY_REQUESTING_LOCATION_UPDATES, false)
    }

    fun setRequestingLocationUpdates(context: Context?, requestingLocationUpdates: Boolean) {
        preferenceHelper.preferences.edit {
            putBoolean(PREF_KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates).apply()
        }
    }
}
