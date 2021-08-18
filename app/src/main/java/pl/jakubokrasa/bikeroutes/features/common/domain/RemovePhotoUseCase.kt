package pl.jakubokrasa.bikeroutes.features.common.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.domain.model.PhotoInfo
import pl.jakubokrasa.bikeroutes.features.common.domain.repository.PhotoRepository

class RemovePhotoUseCase(private val repository: PhotoRepository): UseCase<Unit, PhotoInfo>() {
    override suspend fun action(params: PhotoInfo) {
        repository.removePhoto(params)
    }

}