package pl.jakubokrasa.bikeroutes.core.di

import androidx.lifecycle.SavedStateHandle
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.get
import pl.jakubokrasa.bikeroutes.features.routerecording.data.RouteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.GetCurrentRouteUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.InsertCurrentPointUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.InsertNewRouteUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.MarkRouteAsNotCurrentUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RecordRouteViewModel
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

val featuresModule = module {
    factory<RouteRepository> { RouteRepositoryImpl(get()) }

    factory { GetCurrentRouteUseCase(get())}
    factory { InsertCurrentPointUseCase(get())}
    factory { InsertNewRouteUseCase(get())}
    factory { MarkRouteAsNotCurrentUseCase(get())}

    viewModel { RecordRouteViewModel(get(), get(), get(), get()) }
}


