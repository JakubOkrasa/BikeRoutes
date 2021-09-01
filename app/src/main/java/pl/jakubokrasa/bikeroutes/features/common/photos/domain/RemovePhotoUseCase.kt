package pl.jakubokrasa.bikeroutes.features.common.photos.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.model.PhotoInfo

class RemovePhotoUseCase(private val repository: PhotoRepository): UseCase<Unit, PhotoInfo>() {
    override suspend fun action(params: PhotoInfo) {
        repository.removePhoto(params)
    }

}