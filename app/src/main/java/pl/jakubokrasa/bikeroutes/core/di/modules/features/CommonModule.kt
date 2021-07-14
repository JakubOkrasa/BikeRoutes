package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.common.domain.GetPhotosUseCase
import pl.jakubokrasa.bikeroutes.features.common.domain.GetPointsFromRemoteUseCase
import pl.jakubokrasa.bikeroutes.features.common.presentation.PhotoGalleryAdapter
import pl.jakubokrasa.bikeroutes.features.common.presentation.PhotosRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.GetSegmentsUseCase

val commonModule = module {
    factory { GetPointsFromRemoteUseCase(get()) }
	factory { GetSegmentsUseCase(get()) }
    factory { GetPhotosUseCase(get()) }

    factory { PhotosRecyclerAdapter() }
}