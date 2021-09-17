package pl.jakubokrasa.bikeroutes.features.filter.domain.model

data class GeocodingItem(
    val boundingBoxData: BoundingBoxData,
    val displayName: String,
)