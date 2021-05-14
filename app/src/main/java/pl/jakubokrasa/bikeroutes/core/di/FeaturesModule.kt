package pl.jakubokrasa.bikeroutes.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.map.data.local.LocalRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteViewModel
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.*
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesUseCase

val featuresModule = module {
    factory<LocalRepository> { LocalRepositoryImpl(get()) }

    factory { InsertPointUseCase(get()) }
    factory { GetPointsUseCase(get()) }
    factory { SaveRouteUseCase(get(), get(), get()) }
    factory { GetMyRoutesUseCase(get(), get()) }
    factory { DeletePointsUseCase(get()) }
    factory { UpdateDistanceByPrefsUseCase(get()) }

    factory { pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetPointsFromRemoteUseCase(get()) }

    viewModel { RouteViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    factory { MyRoutesRecyclerAdapter() }
}


