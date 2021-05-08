package pl.jakubokrasa.bikeroutes.core.util

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.util.Log
import androidx.core.content.edit
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.SettingsClient
import org.koin.core.KoinComponent
import org.koin.core.inject
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_REQUESTING_LOCATION_UPDATES

class LocationUtils(private val activity: Activity): KoinComponent {
    private val preferenceHelper: PreferenceHelper by inject()
    private val settingsClient: SettingsClient by inject()
    private val locationSettingsRequest: LocationSettingsRequest by inject()

    fun enableGpsIfNecessary() {
        val locationManager = activity.getSystemService(Service.LOCATION_SERVICE) as LocationManager
        if (!isGpsEnabled(locationManager)) {
            askUserToEnableGps()
        }
    }

    private fun isGpsEnabled(locationManager: LocationManager): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun askUserToEnableGps() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener(activity) {}
            .addOnFailureListener(activity) {
                exception ->
                    when ((exception as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            try {
                                val rae = exception as ResolvableApiException
                                rae.startResolutionForResult(activity, LOCATION_REQUEST)
                            } catch (exp: IntentSender.SendIntentException) {
                                Log.w(LOG_TAG, "Unable to enable GPS, error while resolving LocationSettingsStatusCodes.RESOLUTION_REQUIRED")
                            }
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            Log.e(LOG_TAG, "Unable to enable GPS, LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE")
                        }
                }
            }
    }

    companion object {
        val LOG_TAG = LocationUtils::class.simpleName
        const val LOCATION_REQUEST = 1000
    }
}
