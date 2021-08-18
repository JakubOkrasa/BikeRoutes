package pl.jakubokrasa.bikeroutes.features.common.domain.repository

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.domain.model.PhotoInfo

interface PhotoRepository {
    suspend fun getPhotos(routeId: String): List<PhotoInfo>
    suspend fun addPhoto(routeId: String, localPath: String, sharingType: SharingType)
    suspend fun removePhoto(photoInfo: PhotoInfo)
}