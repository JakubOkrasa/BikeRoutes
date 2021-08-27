package pl.jakubokrasa.bikeroutes.features.common.photos.presentation.model

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.model.PhotoInfo

class PhotoInfoDisplayable(
    val photoId: String,
    val routeId: String,
    val downloadUrl: String,
    val sharingType: SharingType,
    val name: String
) {
    constructor(photoInfo: PhotoInfo): this(
        photoId = photoInfo.photoId,
        routeId = photoInfo.routeId,
        downloadUrl = photoInfo.downloadUrl,
        sharingType = photoInfo.sharingType,
        name = photoInfo.name
    )

    fun toPhotoInfo(): PhotoInfo {
        return PhotoInfo(
            photoId = this.photoId,
            routeId = this.routeId,
            downloadUrl = this.downloadUrl,
            sharingType = this.sharingType,
            name = this.name
        )
    }
}