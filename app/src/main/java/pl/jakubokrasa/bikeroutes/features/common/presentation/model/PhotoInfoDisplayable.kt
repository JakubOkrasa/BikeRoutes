package pl.jakubokrasa.bikeroutes.features.common.presentation.model

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.domain.model.PhotoInfo

class PhotoInfoDisplayable(
    val photoId: String,
    val routeId: String,
    val reference: String,
    val sharingType: SharingType,
) {
    constructor(photoInfo: PhotoInfo): this(
        photoId = photoInfo.photoId,
        routeId = photoInfo.routeId,
        reference = photoInfo.reference,
        sharingType = photoInfo.sharingType
    )

    fun toPhotoInfo(): PhotoInfo {
        return PhotoInfo(
            photoId = this.photoId,
            routeId = this.routeId,
            reference = this.reference,
            sharingType = this.sharingType,
        )
    }
}