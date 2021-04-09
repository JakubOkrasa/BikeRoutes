package pl.jakubokrasa.bikeroutes.features.map.data.remote.model

import org.osmdroid.util.GeoPoint

data class PointResponse(
    val pointId: Long,
    val geoPoint: GeoPoint,
    val createdTime: Long,
    val difficultTerrainStart: Boolean,
    val difficultTerrainEnd: Boolean,
) {}