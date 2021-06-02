package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.map.data.local.LocalRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.*
import pl.jakubokrasa.bikeroutes.features.map.presentation.MapViewModel

val mapModule = module {
    factory<LocalRepository> { LocalRepositoryImpl(get()) }

    factory { InsertPointUseCase(get()) }
    factory { GetPointsUseCase(get()) }
    factory { SaveRouteUseCase(get(), get(), get()) }
    factory { DeletePointsUseCase(get()) }
    factory { UpdateDistanceByPrefsUseCase(get()) }


    viewModel { MapViewModel(get(), get(), get(), get(), get()) }
}