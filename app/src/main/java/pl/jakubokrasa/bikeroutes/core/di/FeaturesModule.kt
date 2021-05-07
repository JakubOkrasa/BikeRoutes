package pl.jakubokrasa.bikeroutes.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.map.data.local.PointRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteViewModel
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.*

val featuresModule = module {
    factory<PointRepository> { PointRepositoryImpl(get()) }

    factory { InsertPointUseCase(get()) }
    factory { GetPointsUseCase(get()) }
    factory { SaveRouteUseCase(get(), get(), get()) }
//    factory { GetMyRoutesUseCase(get())}
//    factory { PutRouteSaveDataUseCase(get(), get())}
    factory { DeletePointsUseCase(get()) }
    factory { UpdateDistanceByPrefsUseCase(get()) }

    viewModel { RouteViewModel(get(), get(), get(), get(), get()) }
    factory { MyRoutesRecyclerAdapter() }
}


