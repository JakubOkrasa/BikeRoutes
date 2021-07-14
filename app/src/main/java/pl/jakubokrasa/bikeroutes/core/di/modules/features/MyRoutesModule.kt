package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.common.domain.GetGeocodingItemUseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.GetSegmentsUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.*
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.DialogSegment
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

val myRoutesModule = module {
    factory { GetMyRoutesUseCase(get(), get()) }
    factory { GetMyRoutesWithFilterUseCase(get(), get()) }
    factory { RemoveRouteUseCase(get()) }
    factory { UpdateRouteUseCase(get()) }
    factory { GetGeocodingItemUseCase(get()) }
    factory { GetSegmentPointUseCase() }
    factory { AddSegmentUseCase(get()) }
    factory { RemoveSegmentUseCase(get()) }

    factory { MyRoutesRecyclerAdapter() }


    viewModel { MyRoutesViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }

}