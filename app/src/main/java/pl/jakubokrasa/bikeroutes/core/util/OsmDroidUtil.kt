package pl.jakubokrasa.bikeroutes.core.util

import android.content.Context
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration

fun configureOsmDroid(context: Context) {
    Configuration.getInstance().load(context,
        PreferenceManager.getDefaultSharedPreferences(context))
}