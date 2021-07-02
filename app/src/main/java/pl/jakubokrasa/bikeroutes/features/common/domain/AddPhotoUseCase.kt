package pl.jakubokrasa.bikeroutes.features.common.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository

class AddPhotoUseCase(private val remoteRepository: RemoteRepository): UseCase<Unit, AddPhotoData>() {
    override suspend fun action(params: AddPhotoData) {
        remoteRepository.addPhoto(params.routeId, params.localPath, params.sharingType)
    }
}

data class AddPhotoData(
    val routeId: String,
    val localPath: String,
    val sharingType: SharingType
)