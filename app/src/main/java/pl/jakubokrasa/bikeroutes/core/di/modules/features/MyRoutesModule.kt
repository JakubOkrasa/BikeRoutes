package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.AddPhotoUseCase
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.GetGeocodingItemUseCase
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.RemovePhotoUseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.GetSegmentPointHelper
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.*
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.ExportRouteHelper
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.AddReviewUseCase
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.RemoveReviewUseCase
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.UpdateReviewUseCase

val myRoutesModule = module {
    factory { GetMyRoutesUseCase(get(), get()) }
    factory { GetMyRoutesWithFilterUseCase(get(), get()) }
    factory { RemoveRouteUseCase(get()) }
    factory { UpdateRouteUseCase(get()) }
    factory { GetGeocodingItemUseCase(get()) }
	factory { AddPhotoUseCase(get()) }
    factory { RemovePhotoUseCase(get()) }
    factory { AddSegmentUseCase(get()) }
    factory { RemoveSegmentUseCase(get()) }
    factory { AddReviewUseCase(get()) }
    factory { UpdateReviewUseCase(get()) }
    factory { RemoveReviewUseCase(get()) }

    factory { ExportRouteHelper(androidContext()) }
    factory { GetSegmentPointHelper() }
    factory { MyRoutesRecyclerAdapter() }

    viewModel { MyRoutesViewModel(get(), get(), get(), get(), get(), get(), get(),
        get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),
        get()) }

}