package pl.jakubokrasa.bikeroutes.core.di.features

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.DeleteRouteUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesWithFilterUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

val myRoutesModule = module {
    factory { GetMyRoutesUseCase(get(), get()) }
    factory { GetMyRoutesWithFilterUseCase(get(), get()) }
    factory { pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetPointsFromRemoteUseCase(get()) }
    factory { DeleteRouteUseCase(get()) }

    factory { MyRoutesRecyclerAdapter() }

    viewModel { MyRoutesViewModel(get(), get(), get(), get(), get()) }
}