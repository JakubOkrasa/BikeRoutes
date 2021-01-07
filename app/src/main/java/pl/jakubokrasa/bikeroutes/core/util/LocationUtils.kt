package pl.jakubokrasa.bikeroutes.core.util

import android.content.Context
import android.content.SharedPreferences

class LocationUtils
    (
    private val preferences: SharedPreferences, private val prefsEditor: SharedPreferences.Editor
)
{
    fun requestingLocationUpdates(context: Context?): Boolean {
        return preferences.getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
    }

    fun setRequestingLocationUpdates(context: Context?, requestingLocationUpdates: Boolean) {
        prefsEditor.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates).apply()
    }

    companion object {
        const val KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates";
    }
}
