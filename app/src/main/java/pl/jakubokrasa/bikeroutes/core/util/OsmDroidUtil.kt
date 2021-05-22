package pl.jakubokrasa.bikeroutes.core.util

import android.content.Context
import android.graphics.Color
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration

fun configureOsmDroid(context: Context) {
    Configuration.getInstance().load(context,
        PreferenceManager.getDefaultSharedPreferences(context))
}


const val routeColor = Color.BLUE
const val routeWidth = 7f