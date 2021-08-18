package pl.jakubokrasa.bikeroutes.features.common.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.domain.repository.PhotoRepository

class AddPhotoUseCase(private val repository: PhotoRepository): UseCase<Unit, AddPhotoData>() {
    override suspend fun action(params: AddPhotoData) {
        repository.addPhoto(params.routeId, params.localPath, params.sharingType)
    }
}

data class AddPhotoData(
    val routeId: String,
    val localPath: String,
    val sharingType: SharingType
)