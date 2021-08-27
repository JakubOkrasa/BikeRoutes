package pl.jakubokrasa.bikeroutes.features.common.photos.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType

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