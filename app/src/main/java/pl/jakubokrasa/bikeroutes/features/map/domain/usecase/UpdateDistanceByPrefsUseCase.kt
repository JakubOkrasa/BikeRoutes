package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import android.location.Location
import androidx.core.content.edit
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.getDouble
import pl.jakubokrasa.bikeroutes.core.extensions.putDouble
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_DISTANCE_SUM
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_LAST_LAT
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_LAST_LNG
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

    private fun getDistanceInMeters(p1: GeoPoint, p2: GeoPoint): Int {
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
