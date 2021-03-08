package pl.jakubokrasa.bikeroutes.features.routerecording.domain.model

import org.osmdroid.util.GeoPoint

class Point(
    val pointId: Long,
    val geoPoint: GeoPoint
) {}