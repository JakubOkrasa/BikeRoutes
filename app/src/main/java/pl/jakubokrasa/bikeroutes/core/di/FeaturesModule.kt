package pl.jakubokrasa.bikeroutes.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.DeleteRouteUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.map.data.RouteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.map.domain.*
import pl.jakubokrasa.bikeroutes.features.map.navigation.MapFrgNavigator
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteViewModel
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteRepository

val featuresModule = module {
    factory<RouteRepository> { RouteRepositoryImpl(get()) }

    factory { GetCurrentRouteUseCase(get())}
    factory { InsertCurrentPointUseCase(get())}
    factory { InsertRouteUseCase(get())}
    factory { MarkRouteAsNotCurrentUseCase(get())}
    factory { GetMyRoutesUseCase(get())}
    factory { PutRouteSaveDataUseCase(get(), get())}
    factory { DeleteRouteUseCase(get())}
    factory { UpdateDistanceByPrefsUseCase(get())}

    viewModel { RouteViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    factory { MyRoutesRecyclerAdapter() }

    factory<MapFrgNavigator> { MapFrgNavigator(get()) }
}


