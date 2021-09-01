package pl.jakubokrasa.bikeroutes.features.common.filter.presentation.model

import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.GeocodingItem

data class GeocodingItemDisplayable(
    val boundingBox: BoundingBoxData,
    val displayName: String,
) {

    fun toGeocodingItem(): GeocodingItem {
        return GeocodingItem(boundingBoxData = this.boundingBox, displayName = this.displayName)
    }

    constructor(geocodingItem: GeocodingItem): this(
        boundingBox = geocodingItem.boundingBoxData,
        displayName = geocodingItem.displayName
    )
}
