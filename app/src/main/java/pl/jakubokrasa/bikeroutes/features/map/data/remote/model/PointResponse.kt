package pl.jakubokrasa.bikeroutes.features.map.data.remote.model

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

data class PointResponse(
    val pointId: Long,
    val geoPoint: GeoPoint,
    val createdTime: Long,
    val difficultTerrainStart: Boolean,
    val difficultTerrainEnd: Boolean,
) 
{
    constructor(point: Point): this (
        pointId = point.pointId,
        createdTime = point.createdAt,
        geoPoint = point.geoPoint,
        difficultTerrainStart = false, //todo
        difficultTerrainEnd = false //todo


    )
}