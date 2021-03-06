package pl.jakubokrasa.bikeroutes.core.di.modules

import androidx.navigation.navOptions
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigatorImpl
import pl.jakubokrasa.bikeroutes.core.provider.ActivityProvider
import pl.jakubokrasa.bikeroutes.features.ui_features.map.navigation.MapFrgNavigator
import pl.jakubokrasa.bikeroutes.features.ui_features.myroutes.navigation.MyRoutesNavigator
import pl.jakubokrasa.bikeroutes.features.ui_features.routedetails.navigation.CommonRoutesNavigator
import pl.jakubokrasa.bikeroutes.features.ui_features.sharedroutes.navigation.SharedRoutesNavigator

val navigationModule = module {
    single(createdAtStart = true) { ActivityProvider(androidApplication()) }
    factory<FragmentNavigator> { FragmentNavigatorImpl(
        get(),
        navHostFragmentRes = R.id.nav_host_fragment,
        homeDestinationRes = R.id.nav_map,
        defaultNavOptions = get()
    ) }

    single {
        MapFrgNavigator(
            get())
    }
    single { MyRoutesNavigator(get()) }
    single { SharedRoutesNavigator(get()) }
    single { CommonRoutesNavigator(get()) }

    factory {
        navOptions {
            anim { enter = R.anim.nav_default_enter_anim }
            anim { exit = R.anim.nav_default_exit_anim }
            anim { popEnter = R.anim.nav_default_pop_enter_anim }
            anim { popExit = R.anim.nav_default_pop_exit_anim }
        }
    }
}