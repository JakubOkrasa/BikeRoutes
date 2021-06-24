package pl.jakubokrasa.bikeroutes.features.common.data.model

import com.google.gson.annotations.SerializedName
import pl.jakubokrasa.bikeroutes.features.common.domain.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.common.domain.model.GeocodingItem

data class GeocodingItemResponse(
    @SerializedName("boundingbox")
    val boundingBox: List<Double>,

    @SerializedName("display_name")
    val displayName: String,
) {

    fun toGeocodingItem(): GeocodingItem {
        return GeocodingItem(
            boundingBoxData = BoundingBoxData(this.boundingBox[0], this.boundingBox[1], this.boundingBox[2], this.boundingBox[3]),
            displayName = this.displayName
        )
    }

}
