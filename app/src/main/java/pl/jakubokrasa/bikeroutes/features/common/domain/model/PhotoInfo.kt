package pl.jakubokrasa.bikeroutes.features.common.domain.model

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType

data class PhotoInfo(
    val photoId: String,
    val routeId: String,
    val reference: String,
    val sharingType: SharingType,
) {}