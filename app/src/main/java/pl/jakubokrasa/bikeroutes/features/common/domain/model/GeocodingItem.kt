package pl.jakubokrasa.bikeroutes.features.common.domain.model

data class GeocodingItem(
    val boundingBoxData: BoundingBoxData,

    val displayName: String,
)