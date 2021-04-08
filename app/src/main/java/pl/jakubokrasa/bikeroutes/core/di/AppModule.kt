package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context
import android.location.LocationManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper

val appModule = module {
    factory { DividerItemDecoration(androidContext(), DividerItemDecoration.VERTICAL) }
    single { androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    single { LocalBroadcastManager.getInstance(androidContext())}
    single { PreferenceHelper(androidContext()) }
}