package pl.jakubokrasa.bikeroutes.features.ui_features.map.presentation

import android.location.Location
import androidx.core.content.edit
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.presentation.Helper
import pl.jakubokrasa.bikeroutes.core.extensions.getDouble
import pl.jakubokrasa.bikeroutes.core.extensions.putDouble
import pl.jakubokrasa.bikeroutes.core.util.PreferenceHelper
import kotlin.math.roundToInt

class UpdateDistanceHelper(private val preferenceHelper: PreferenceHelper): Helper<Unit, GeoPoint>() {
    override suspend fun action(params: GeoPoint) {
        updateDistance(params)
    }

    private suspend fun updateDistance(newGeoPoint: GeoPoint) {
        with(preferenceHelper.preferences) {
            val currentSum = getInt(PreferenceHelper.PREF_KEY_DISTANCE_SUM, 0)
            if (contains(PreferenceHelper.PREF_KEY_LAST_LAT) && contains(PreferenceHelper.PREF_KEY_LAST_LNG)) {
                val lastLat = getDouble(PreferenceHelper.PREF_KEY_LAST_LAT, 0.0)
                val lastLng = getDouble(PreferenceHelper.PREF_KEY_LAST_LNG, 0.0)

                edit {
                    val distanceFromLastPoint =
                        getDistanceInMeters(GeoPoint(lastLat, lastLng), newGeoPoint)
                    val sum = currentSum + distanceFromLastPoint
                    putInt(PreferenceHelper.PREF_KEY_DISTANCE_SUM, sum)
                }
            }
            edit {
                putDouble(PreferenceHelper.PREF_KEY_LAST_LAT, newGeoPoint.latitude)
                putDouble(PreferenceHelper.PREF_KEY_LAST_LNG, newGeoPoint.longitude)
            }
        }

    }

    private suspend fun getDistanceInMeters(p1: GeoPoint, p2: GeoPoint): Int {
        val output = FloatArray(1)
        Location.distanceBetween(
            p1.latitude,
            p1.longitude,
            p2.latitude,
            p2.longitude,
            output)
        return output[0].roundToInt()
    }

}