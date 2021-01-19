package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context
import android.location.LocationManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single { androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    single { LocalBroadcastManager.getInstance(androidContext())}
}