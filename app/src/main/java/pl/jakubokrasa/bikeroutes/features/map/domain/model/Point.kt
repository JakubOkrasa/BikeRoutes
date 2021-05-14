package pl.jakubokrasa.bikeroutes.features.map.domain.model

import org.osmdroid.util.GeoPoint
import java.io.Serializable

class Point(
    val pointId: Long,
    val geoPoint: GeoPoint,
    val createdAt: Long,
    val difficultTerrainStart: Boolean,
    val difficultTerrainEnd: Boolean,
): Serializable {}