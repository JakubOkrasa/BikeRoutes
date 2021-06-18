package pl.jakubokrasa.bikeroutes.features.common.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class FilterData(
    val minDistanceKm: Int? = null,
    val maxDistanceKm: Int? = null,
    val boundingBoxData: BoundingBoxData? = null
)

@Parcelize
data class BoundingBoxData(
    val latNorth: Double,
    val latSouth: Double,
    val lonEast: Double,
    val lonWest: Double
): Parcelable {

    constructor(): this (0.0, 0.0, 0.0, 0.0)
}