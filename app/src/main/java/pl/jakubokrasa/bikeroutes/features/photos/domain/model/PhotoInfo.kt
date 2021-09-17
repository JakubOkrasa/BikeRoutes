package pl.jakubokrasa.bikeroutes.features.photos.domain.model

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType

data class PhotoInfo(
    val photoId: String,
    val routeId: String,
    val downloadUrl: String,
    val sharingType: SharingType,
    val name: String
)