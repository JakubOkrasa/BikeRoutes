package pl.jakubokrasa.bikeroutes.features.common.photos.data.model

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.model.PhotoInfo

data class PhotoInfoResponse(
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

    constructor(): this("", "", "", pl.jakubokrasa.bikeroutes.core.util.enums.SharingType.PUBLIC, "")

    fun toPhotoInfo(): PhotoInfo {
        return PhotoInfo(
            this.photoId,
            this.routeId,
            this.downloadUrl,
            this.sharingType,
            this.name
        )
    }
}
