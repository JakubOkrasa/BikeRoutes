package pl.jakubokrasa.bikeroutes.core.di

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.core.util.LocationUtils
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.LocationService


val locationModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext());}
    single {LocationUtils(get(), get())}
    single { createLocationRequest() }
}

private fun createLocationRequest() =
    LocationRequest()
        .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
        .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

const val UPDATE_INTERVAL_IN_MILLISECONDS = 4_000L;
const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2