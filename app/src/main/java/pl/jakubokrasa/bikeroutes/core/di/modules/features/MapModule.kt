package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.common.points.data.local.PointLocalRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.common.points.domain.PointLocalRepository
import pl.jakubokrasa.bikeroutes.features.common.points.domain.DeletePointsUseCase
import pl.jakubokrasa.bikeroutes.features.common.points.domain.GetPointsFromLocalSyncUseCase
import pl.jakubokrasa.bikeroutes.features.common.points.domain.InsertPointUseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.SaveRouteUseCase
import pl.jakubokrasa.bikeroutes.features.map.presentation.MapViewModel
import pl.jakubokrasa.bikeroutes.features.map.presentation.UpdateDistanceHelper

val mapModule = module {
    factory<PointLocalRepository> { PointLocalRepositoryImpl(get()) }

    factory { InsertPointUseCase(get()) }
    factory { GetPointsFromLocalSyncUseCase(get()) }
    factory { SaveRouteUseCase(get(), get(), get()) }
    factory { DeletePointsUseCase(get()) }

    factory { UpdateDistanceHelper(get()) }
    viewModel { MapViewModel(get(), get(), get(), get(), get()) }
}