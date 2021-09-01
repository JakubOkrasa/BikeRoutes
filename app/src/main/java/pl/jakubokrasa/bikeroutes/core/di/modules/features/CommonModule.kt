package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.GetPhotosUseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.GetPointsFromRemoteUseCase
import pl.jakubokrasa.bikeroutes.features.common.photos.data.PhotoRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.PhotoRepository
import pl.jakubokrasa.bikeroutes.features.common.photos.presentation.PhotosRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.common.reviews.data.ReviewRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.GetSegmentsUseCase
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.GetReviewsUseCase
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.ReviewRepository
import pl.jakubokrasa.bikeroutes.features.common.reviews.presentation.ReviewsRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.common.routes.data.RouteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.common.segments.data.SegmentRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.SegmentRepository
import pl.jakubokrasa.bikeroutes.features.map.data.remote.PointRemoteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRemoteRepository

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

    factory { PhotosRecyclerAdapter() }
    factory { ReviewsRecyclerAdapter() }
}