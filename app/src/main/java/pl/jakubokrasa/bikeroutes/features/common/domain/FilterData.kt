package pl.jakubokrasa.bikeroutes.features.common.domain


data class FilterData(
    val minDistanceKm: Int? = null,
    val maxDistanceKm: Int? = null,
    val boundingBoxData: BoundingBoxData? = null
)

data class BoundingBoxData(
    val latNorth: Double,
    val latSouth: Double,
    val lonEast: Double,
    val lonWest: Double
)