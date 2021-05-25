package pl.jakubokrasa.bikeroutes.core.extensions

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import pl.jakubokrasa.bikeroutes.BuildConfig

class PreferenceHelper(ctx: Context) {

    companion object {
        const val PREF_KEY_DISTANCE_SUM = "distance_sum"
        const val PREF_KEY_LAST_LAT = "last_lat"
        const val PREF_KEY_LAST_LNG = "last_lng"
        const val PREF_KEY_REQUESTING_LOCATION_UPDATES = "requesting_location_updates"
        const val PREF_KEY_MAPFRAGMENT_MODE_RECORDING = "mapfragment_recording"

        const val PREF_KEY_USER_EMAIL = "user_email"
        const val PREF_KEY_USER_PASSWORD = "user_password"
    }

    private val filePath = BuildConfig.APPLICATION_ID + ".preferences"
    val preferences: SharedPreferences = ctx.getSharedPreferences(filePath, Context.MODE_PRIVATE)

    fun saveUserDataToSharedPreferences(email: String, password: String) {
        this.preferences.edit {
            putString(PreferenceHelper.PREF_KEY_USER_EMAIL, email)
            putString(PreferenceHelper.PREF_KEY_USER_PASSWORD, password)
        }
    }

    fun deleteUserDataFromSharedPreferences() {
        this.preferences.edit {
            remove(PreferenceHelper.PREF_KEY_USER_EMAIL)
            remove(PreferenceHelper.PREF_KEY_USER_PASSWORD)
        }
    }

}

//todo source: https://stackoverflow.com/a/45412036/9343040
fun SharedPreferences.Editor.putDouble(key: String, double: Double) =
    putLong(key, java.lang.Double.doubleToRawLongBits(double))

fun SharedPreferences.getDouble(key: String, default: Double) =
    java.lang.Double.longBitsToDouble(getLong(key, java.lang.Double.doubleToRawLongBits(default)))