package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.photos.data.PhotoRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.photos.domain.GetPhotosUseCase
import pl.jakubokrasa.bikeroutes.features.photos.domain.PhotoRepository
import pl.jakubokrasa.bikeroutes.features.photos.presentation.PhotosRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.points.data.remote.PointRemoteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.points.domain.GetPointsFromRemoteUseCase
import pl.jakubokrasa.bikeroutes.features.points.domain.PointRemoteRepository
import pl.jakubokrasa.bikeroutes.features.reviews.data.ReviewRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.reviews.domain.GetReviewsUseCase
import pl.jakubokrasa.bikeroutes.features.reviews.domain.ReviewRepository
import pl.jakubokrasa.bikeroutes.features.reviews.presentation.ReviewsRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.routes.data.RouteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.segments.data.SegmentRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.segments.domain.GetSegmentsUseCase
import pl.jakubokrasa.bikeroutes.features.segments.domain.SegmentRepository

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