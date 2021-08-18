package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.common.data.*
import pl.jakubokrasa.bikeroutes.features.common.domain.GetPhotosUseCase
import pl.jakubokrasa.bikeroutes.features.common.domain.GetPointsFromRemoteUseCase
import pl.jakubokrasa.bikeroutes.features.common.domain.repository.*
import pl.jakubokrasa.bikeroutes.features.common.presentation.PhotosRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.GetSegmentsUseCase
import pl.jakubokrasa.bikeroutes.features.reviews.domain.GetReviewsUseCase
import pl.jakubokrasa.bikeroutes.features.reviews.presentation.ReviewsRecyclerAdapter

val commonModule = module {
    factory<PointRemoteRepository> { PointRemoteRepositoryImpl(get()) }
    factory<RouteRepository> { RouteRepositoryImpl(get()) }
    factory<PhotoRepository> { PhotoRepositoryImpl(get(), get()) }
    factory<SegmentRepository> { SegmentRepositoryImpl(get()) }
    factory<ReviewRepository> { ReviewRepositoryImpl(get()) }

    factory { GetPointsFromRemoteUseCase(get()) }
	factory { GetSegmentsUseCase(get()) }
    factory { GetPhotosUseCase(get()) }
    factory { GetReviewsUseCase(get()) }

    factory { PhotosRecyclerAdapter()}
    factory { ReviewsRecyclerAdapter() }
}