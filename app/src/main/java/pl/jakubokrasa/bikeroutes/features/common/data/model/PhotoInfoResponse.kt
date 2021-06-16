package pl.jakubokrasa.bikeroutes.features.common.data.model

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.domain.model.PhotoInfo

data class PhotoInfoResponse(
    val routeId: String,
    val reference: String,
    val sharingType: SharingType,
) {
    constructor(photoInfo: PhotoInfo): this(
        routeId = photoInfo.routeId,
        reference = photoInfo.reference,
        sharingType = photoInfo.sharingType,
    )

    constructor(): this("", "", pl.jakubokrasa.bikeroutes.core.util.enums.SharingType.PUBLIC)

    fun toPhotoInfo(): PhotoInfo {
        return PhotoInfo(
            this.routeId,
            this.reference,
            this.sharingType
        )
    }
}
