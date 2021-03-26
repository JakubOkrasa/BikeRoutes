package pl.jakubokrasa.bikeroutes.core.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.ui.MyRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.routerecording.data.RouteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.*
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteViewModel
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

val featuresModule = module {
    factory<RouteRepository> { RouteRepositoryImpl(get()) }

    factory { GetCurrentRouteUseCase(get())}
    factory { InsertCurrentPointUseCase(get())}
    factory { InsertNewRouteUseCase(get())}
    factory { MarkRouteAsNotCurrentUseCase(get())}
    factory { GetMyRoutesUseCase(get())}
    factory { PutRouteSaveDataUseCase(get())}

//    factory<RvItemClickListener> { MyRoutesFragment() }

    viewModel { RouteViewModel(get(), get(), get(), get(), get(), get()) }
    factory { MyRoutesRecyclerAdapter() }
}


