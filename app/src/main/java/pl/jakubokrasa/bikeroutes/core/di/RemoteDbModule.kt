package pl.jakubokrasa.bikeroutes.core.di

import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.map.data.remote.RouteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.map.domain.RouteRepository

val remoteDbModule = module {
    factory<RouteRepository> { RouteRepositoryImpl(get()) }
}