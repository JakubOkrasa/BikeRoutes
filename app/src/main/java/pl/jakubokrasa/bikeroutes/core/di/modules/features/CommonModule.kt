package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.common.domain.GetPhotosUseCase
import pl.jakubokrasa.bikeroutes.features.common.domain.GetPointsFromRemoteUseCase
import pl.jakubokrasa.bikeroutes.features.common.presentation.PhotoGalleryAdapter
import pl.jakubokrasa.bikeroutes.features.common.presentation.PhotosRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.GetSegmentsUseCase
import pl.jakubokrasa.bikeroutes.features.reviews.domain.GetReviewsUseCase
import pl.jakubokrasa.bikeroutes.features.reviews.presentation.ReviewsRecyclerAdapter

val commonModule = module {
    factory { GetPointsFromRemoteUseCase(get()) }
	factory { GetSegmentsUseCase(get()) }
    factory { GetPhotosUseCase(get()) }
    factory { GetReviewsUseCase(get()) }

    factory { PhotosRecyclerAdapter()}
    factory { ReviewsRecyclerAdapter() }
}