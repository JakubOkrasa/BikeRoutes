package pl.jakubokrasa.bikeroutes.core.util

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
        const val PREF_KEY_USER_UID = "user_uid"
        const val PREF_KEY_USER_DISPLAY_NAME = "user_display_name"

        const val PREF_KEY_TIPS_SEGMENTS_NOT_SHOWED = "key_tips_segments_not_showed"
        const val PREF_KEY_TIPS_SEGMENTS_DETAILS_NOT_SHOWED = "key_tips_segments_details_not_showed"
    }

    private val filePath = BuildConfig.APPLICATION_ID + ".preferences"
    val preferences: SharedPreferences = ctx.getSharedPreferences(filePath, Context.MODE_PRIVATE)

    fun saveUserDataToSharedPreferences(email: String, password: String, uid: String) {
        this.preferences.edit {
            putString(PREF_KEY_USER_EMAIL, email)
            putString(PREF_KEY_USER_PASSWORD, password)
            putString(PREF_KEY_USER_UID, uid)
        }
    }

    fun deleteUserDataFromSharedPreferences() {
        this.preferences.edit {
            remove(PREF_KEY_USER_EMAIL)
            remove(PREF_KEY_USER_PASSWORD)
            remove(PREF_KEY_USER_UID)
            remove(PREF_KEY_USER_DISPLAY_NAME)
        }
    }

    fun saveDisplayNameToSharedPreferences(displayName: String) {
        this.preferences.edit() {
            putString(PREF_KEY_USER_DISPLAY_NAME, displayName)
        }
    }

}

fun getCurrentUserUid(preferenceHelper: PreferenceHelper) =  preferenceHelper.preferences.getString(PreferenceHelper.PREF_KEY_USER_UID, "")!!

fun getCurrentUserDisplayName(preferenceHelper: PreferenceHelper) =  preferenceHelper.preferences.getString(PreferenceHelper.PREF_KEY_USER_DISPLAY_NAME, "unknown-user")!!
