package pl.jakubokrasa.bikeroutes.features.common.filter.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class FilterData(
    var minDistanceKm: Int? = null,
    var maxDistanceKm: Int? = null,
    val boundingBoxData: BoundingBoxData? = null
)

@Parcelize
data class BoundingBoxData(
    val latSouth: Double,
    val latNorth: Double,
    val lonWest: Double,
    val lonEast: Double
): Parcelable {

    constructor(): this (0.0, 0.0, 0.0, 0.0)
}