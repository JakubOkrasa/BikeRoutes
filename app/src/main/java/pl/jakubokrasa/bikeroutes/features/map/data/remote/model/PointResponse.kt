package pl.jakubokrasa.bikeroutes.features.map.data.remote.model

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.myroutes.data.model.GeoPointResponse

data class PointResponse(
    val pointId: Long,
    val geoPoint: GeoPointResponse,
    val createdTime: Long,
    val difficultTerrainStart: Boolean,
    val difficultTerrainEnd: Boolean,
) 
{
    constructor(point: Point): this (
        pointId = point.pointId,
        createdTime = point.createdAt,
        geoPoint = GeoPointResponse(point.geoPoint),
        difficultTerrainStart = point.difficultTerrainStart,
        difficultTerrainEnd = point.difficultTerrainStart


    )

    constructor(): this(0, GeoPointResponse(0.0, 0.0), 0, false, false)

    fun toPoint(): Point {
        return Point(
            this.pointId,
            GeoPoint(this.geoPoint.latitude, this.geoPoint.longitude),
            this.createdTime,
            this.difficultTerrainStart,
            this.difficultTerrainEnd
        )
    }
}