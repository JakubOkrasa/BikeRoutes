package pl.jakubokrasa.bikeroutes.core.di

import org.koin.core.module.Module

val koinInjector: List<Module> = listOf(
    appModule,
    locationModule,
    preferencesModule,
)