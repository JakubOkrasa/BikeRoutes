package pl.jakubokrasa.bikeroutes.features.common.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.domain.model.PhotoInfo
import pl.jakubokrasa.bikeroutes.features.common.domain.repository.PhotoRepository

class GetPhotosUseCase(private val repository: PhotoRepository): UseCase<List<PhotoInfo>, String>() {
    override suspend fun action(params: String): List<PhotoInfo> {
        return repository.getPhotos(params)
    }
}