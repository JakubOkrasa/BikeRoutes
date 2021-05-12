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
        difficultTerrainStart = false, //todo
        difficultTerrainEnd = false //todo


    )

    constructor(): this(0, GeoPointResponse(0.0, 0.0), 0, false, false)
}