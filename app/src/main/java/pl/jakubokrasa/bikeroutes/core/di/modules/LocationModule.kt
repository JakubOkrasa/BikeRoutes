package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val locationModule = module {
    single { androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    single { LocationServices.getFusedLocationProviderClient(androidContext());}
    single { createLocationRequest() }
    single { LocationServices.getSettingsClient(androidContext()) }
    single { LocationSettingsRequest.Builder().addLocationRequest(get()).build() }
}

private fun createLocationRequest() =
    LocationRequest()
        .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
        .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//        .setSmallestDisplacement(5f)

const val UPDATE_INTERVAL_IN_MILLISECONDS = 4_000L;
const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2