package pl.jakubokrasa.bikeroutes.features.photos.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.photos.domain.model.PhotoInfo

class GetPhotosUseCase(private val repository: PhotoRepository): UseCase<List<PhotoInfo>, String>() {
    override suspend fun action(params: String): List<PhotoInfo> {
        return repository.getPhotos(params)
    }
}