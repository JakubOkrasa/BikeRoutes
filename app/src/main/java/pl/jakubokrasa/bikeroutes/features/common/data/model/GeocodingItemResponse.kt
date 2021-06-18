package pl.jakubokrasa.bikeroutes.features.common.data.model

import com.google.gson.annotations.SerializedName
import pl.jakubokrasa.bikeroutes.features.common.domain.model.GeocodingItem

data class GeocodingItemResponse(
    @SerializedName("boundingbox")
    val boundingBox: List<Float>,

    @SerializedName("display_name")
    val displayName: String,
) {

    fun toGeocodingItem(): GeocodingItem {
        return GeocodingItem(
            boundingBox = this.boundingBox,
            displayName = this.displayName
        )
    }

}
