package pl.jakubokrasa.bikeroutes.features.map.data.remote.model

import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.myroutes.data.model.GeoPointDataResponse

data class PointResponse(
    val pointId: Long,
    val geoPointData: GeoPointDataResponse,
    val createdTime: Long,
)
{
    constructor(point: Point): this (
        pointId = point.pointId,
        createdTime = point.createdAt,
        geoPointData = GeoPointDataResponse(point.geoPointData)
    )

    constructor(): this(0, GeoPointDataResponse(0.0, 0.0), 0)

    fun toPoint(): Point {
        return Point(
            this.pointId,
            GeoPointData(this.geoPointData.latitude, this.geoPointData.longitude),
            this.createdTime,
        )
    }
}