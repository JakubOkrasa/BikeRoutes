package pl.jakubokrasa.bikeroutes.core.di

import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.map.data.remote.RemoteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository

val remoteDbModule = module {
    factory<RemoteRepository> { RemoteRepositoryImpl(get(), get()) }
}