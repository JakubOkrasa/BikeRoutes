package pl.jakubokrasa.bikeroutes.features.map.domain

import android.location.Location
import androidx.core.content.edit
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extentions.getDouble
import pl.jakubokrasa.bikeroutes.core.extentions.putDouble
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper.Companion.PREF_KEY_DISTANCE_SUM
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper.Companion.PREF_KEY_LAST_LAT
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper.Companion.PREF_KEY_LAST_LNG
import kotlin.math.roundToInt

class UpdateDistanceByPrefsUseCase(private val preferenceHelper: PreferenceHelper) :
    UseCase<Unit, GeoPoint>() {
    override suspend fun action(params: GeoPoint) {
        updateDistance(params)
    }

    private fun updateDistance(newGeoPoint: GeoPoint) { //todo consider moving it to data layer
        with(preferenceHelper.preferences) {
            val currentSum = getInt(PREF_KEY_DISTANCE_SUM, 0)
            if (contains(PREF_KEY_LAST_LAT) && contains(PREF_KEY_LAST_LNG)) {
                val lastLat = getDouble(PREF_KEY_LAST_LAT, 0.0)
                val lastLng = getDouble(PREF_KEY_LAST_LNG, 0.0)

                edit {
                    val distanceFromLastPoint =
                        getDistanceInMeters(GeoPoint(lastLat, lastLng), newGeoPoint)
                    val sum = currentSum + distanceFromLastPoint
                    putInt(PREF_KEY_DISTANCE_SUM, sum)
                }
            }
            edit {
                putDouble(PREF_KEY_LAST_LAT, newGeoPoint.latitude)
                putDouble(PREF_KEY_LAST_LNG, newGeoPoint.longitude)
            }
        }

    }

    private fun getDistanceInMeters(
        p1: GeoPoint, p2: GeoPoint
    ): Int { // not the original function, original in npp
        val lat1 = p1.latitude
        val lng1 = p1.longitude
        val lat2 = p2.latitude
        val lng2 = p2.longitude
        val dist = FloatArray(1)
        Location.distanceBetween(lat1, lng1, lat2, lng2, dist)
        return dist[0].roundToInt()
    }

}
