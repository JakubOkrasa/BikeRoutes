package pl.jakubokrasa.bikeroutes.features.routerecording.domain.model

import org.osmdroid.util.GeoPoint
import java.io.Serializable

class Point(
    val pointId: Long,
    val geoPoint: GeoPoint
): Serializable {}