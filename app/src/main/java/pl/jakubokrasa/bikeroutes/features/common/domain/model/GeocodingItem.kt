package pl.jakubokrasa.bikeroutes.features.common.domain.model

import com.google.gson.annotations.SerializedName
import pl.jakubokrasa.bikeroutes.features.common.domain.BoundingBoxData

data class GeocodingItem(
    val boundingBoxData: BoundingBoxData,

    val displayName: String,
)