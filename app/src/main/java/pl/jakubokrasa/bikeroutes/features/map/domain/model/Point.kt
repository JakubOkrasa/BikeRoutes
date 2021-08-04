package pl.jakubokrasa.bikeroutes.features.map.domain.model

import java.io.Serializable

class Point(
    val pointId: Long,
    val geoPointData: GeoPointData,
    val createdAt: Long,
): Serializable {}

data class GeoPointData(
    val latitude: Double,
    val longitude: Double
) {
    constructor(): this(0.0, 0.0) //for firestore
}