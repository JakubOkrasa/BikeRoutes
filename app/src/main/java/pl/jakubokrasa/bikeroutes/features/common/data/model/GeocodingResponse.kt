package pl.jakubokrasa.bikeroutes.features.common.data.model

import org.osmdroid.util.BoundingBox

data class GeocodingResponse(
    val results: List<GeocodingItem>

)

data class GeocodingItem(
    val boundingbox: BoundingBox,
    val displayName: String,
)
