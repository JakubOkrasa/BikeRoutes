package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.common.domain.GetGeocodingItemUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesWithFilterUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.RemoveRouteUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.UpdateRouteUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

val myRoutesModule = module {
    factory { GetMyRoutesUseCase(get(), get()) }
    factory { GetMyRoutesWithFilterUseCase(get(), get()) }
    factory { RemoveRouteUseCase(get()) }
    factory { UpdateRouteUseCase(get()) }
    factory { GetGeocodingItemUseCase(get()) }

    factory { MyRoutesRecyclerAdapter() }

    viewModel { MyRoutesViewModel(get(), get(), get(), get(), get(), get(), get()) }

}