package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.filter.domain.GetGeocodingItemUseCase
import pl.jakubokrasa.bikeroutes.features.photos.domain.AddPhotoUseCase
import pl.jakubokrasa.bikeroutes.features.photos.domain.RemovePhotoUseCase
import pl.jakubokrasa.bikeroutes.features.reviews.domain.AddReviewUseCase
import pl.jakubokrasa.bikeroutes.features.reviews.domain.RemoveReviewUseCase
import pl.jakubokrasa.bikeroutes.features.reviews.domain.UpdateReviewUseCase
import pl.jakubokrasa.bikeroutes.features.routes.domain.RemoveRouteUseCase
import pl.jakubokrasa.bikeroutes.features.routes.domain.UpdateRouteUseCase
import pl.jakubokrasa.bikeroutes.features.segments.domain.AddSegmentUseCase
import pl.jakubokrasa.bikeroutes.features.segments.domain.RemoveSegmentUseCase
import pl.jakubokrasa.bikeroutes.features.segments.presentation.GetSegmentPointHelper
import pl.jakubokrasa.bikeroutes.features.ui_features.myroutes.domain.GetMyRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.ui_features.myroutes.domain.GetMyRoutesWithFilterUseCase
import pl.jakubokrasa.bikeroutes.features.ui_features.myroutes.presentation.MyRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.ui_features.myroutes.presentation.MyRoutesViewModel
import pl.jakubokrasa.bikeroutes.features.ui_features.routedetails.presentation.ExportRouteHelper

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