package pl.jakubokrasa.bikeroutes.features.common.domain.model

import com.google.gson.annotations.SerializedName

data class GeocodingItem(
    val boundingBox: List<Float>,

    val displayName: String,
)