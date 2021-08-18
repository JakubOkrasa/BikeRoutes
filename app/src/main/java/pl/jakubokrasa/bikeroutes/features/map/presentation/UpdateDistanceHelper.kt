package pl.jakubokrasa.bikeroutes.features.map.presentation

import android.location.Location
import androidx.core.content.edit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.getDouble
import pl.jakubokrasa.bikeroutes.core.extensions.putDouble
import kotlin.math.roundToInt

class UpdateDistanceHelper(private val preferenceHelper: PreferenceHelper) {
    suspend fun action(geoPoint: GeoPoint) {
        updateDistance(geoPoint)
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

    operator fun invoke(
        geoPoint: GeoPoint, scope: CoroutineScope, onResult: (Result<Unit>) -> Unit = {}
    ) {
        scope.launch {
            val result = withContext(Dispatchers.Default) {
                runCatching { action(geoPoint) }
            }
            onResult(result)
        }
    }

}