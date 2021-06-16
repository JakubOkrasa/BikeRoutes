package pl.jakubokrasa.bikeroutes.features.common.data.model

import com.google.gson.annotations.SerializedName

data class GeocodingResponse(
    val results: Array<GeocodingItem>

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GeocodingResponse

        if (!results.contentEquals(other.results)) return false

        return true
    }

    override fun hashCode(): Int {
        return results.contentHashCode()
    }
}

data class GeocodingItem(
    @SerializedName("boundingbox")
    val boundingBox: List<Float>,

    @SerializedName("display_name")
    val displayName: String,
)
