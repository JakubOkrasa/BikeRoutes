package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context
import android.location.LocationManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigatorImpl
import pl.jakubokrasa.bikeroutes.core.provider.ActivityProvider
import pl.jakubokrasa.bikeroutes.features.map.navigation.MapFrgNavigator
import pl.jakubokrasa.bikeroutes.features.myroutes.navigation.MyRoutesNavigator

val appModule = module {
    factory { DividerItemDecoration(androidContext(), DividerItemDecoration.VERTICAL) }
    single { androidContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager }
    single { LocalBroadcastManager.getInstance(androidContext())}
    single { PreferenceHelper(androidContext()) }
}