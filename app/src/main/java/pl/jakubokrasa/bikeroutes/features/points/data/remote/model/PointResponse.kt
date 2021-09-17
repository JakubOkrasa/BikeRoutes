package pl.jakubokrasa.bikeroutes.features.points.data.remote.model

import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

data class PointResponse(
    val pointId: Long,
    val geoPointData: GeoPointData,
    val createdTime: Long,
)
{
    constructor(point: Point): this (
        pointId = point.pointId,
        createdTime = point.createdAt,
        geoPointData = point.geoPointData
    )

    constructor(): this(0, GeoPointData(0.0, 0.0), 0) //for Firestore

    fun toPoint(): Point {
        return Point(
            this.pointId,
            GeoPointData(this.geoPointData.latitude, this.geoPointData.longitude),
            this.createdTime,
        )
    }
}