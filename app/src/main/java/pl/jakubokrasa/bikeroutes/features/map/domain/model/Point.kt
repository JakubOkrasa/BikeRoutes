package pl.jakubokrasa.bikeroutes.features.map.domain.model

import org.osmdroid.util.GeoPoint
import java.io.Serializable
import java.util.*

class Point(
    val pointId: Long,
    val geoPoint: GeoPoint,
    val createdAt: Long
): Serializable {}