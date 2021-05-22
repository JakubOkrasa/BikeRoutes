package pl.jakubokrasa.bikeroutes.core.di

import org.koin.core.module.Module
import pl.jakubokrasa.bikeroutes.core.di.features.mapModule
import pl.jakubokrasa.bikeroutes.core.di.features.myRoutesModule

val koinInjector: List<Module> = listOf(
    appModule,
    locationModule,
    preferencesModule,
    localDbModule,
    navigationModule,
    featuresModule,
	userModule,
    remoteDbModule,

    mapModule,
    myRoutesModule,
)