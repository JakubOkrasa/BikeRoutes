package pl.jakubokrasa.bikeroutes.core.di

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinApiExtension
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.core.util.LocationUtils
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.LocationService


@OptIn(KoinApiExtension::class)
val locationModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidContext());}
    single {LocationUtils(get(), get())}
    single {LocationService()}
    single { createLocationRequest() }

}

@KoinApiExtension
private fun createLocationRequest(): LocationRequest {
    val locationRequest = LocationRequest()
    locationRequest.interval = LocationService.UPDATE_INTERVAL_IN_MILLISECONDS
    locationRequest.fastestInterval = LocationService.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    return locationRequest
}