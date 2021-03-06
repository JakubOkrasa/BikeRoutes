package pl.jakubokrasa.bikeroutes.core.di

import org.koin.core.module.Module
import pl.jakubokrasa.bikeroutes.core.di.modules.*
import pl.jakubokrasa.bikeroutes.core.di.modules.features.commonModule
import pl.jakubokrasa.bikeroutes.core.di.modules.features.mapModule
import pl.jakubokrasa.bikeroutes.core.di.modules.features.myRoutesModule
import pl.jakubokrasa.bikeroutes.core.di.modules.features.sharedRoutesModule

val koinInjector: List<Module> = listOf(
    appModule,
    locationModule,
    preferencesModule,
    localDbModule,
    navigationModule,
	userModule,
	cloudStorageModule,
    networkModule,

    commonModule,
    mapModule,
    myRoutesModule,
    sharedRoutesModule,
)