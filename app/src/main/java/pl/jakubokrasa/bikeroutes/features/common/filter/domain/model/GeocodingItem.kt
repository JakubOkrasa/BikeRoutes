package pl.jakubokrasa.bikeroutes.features.common.filter.domain.model

import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.BoundingBoxData

data class GeocodingItem(
    val boundingBoxData: BoundingBoxData,

    val displayName: String,
)