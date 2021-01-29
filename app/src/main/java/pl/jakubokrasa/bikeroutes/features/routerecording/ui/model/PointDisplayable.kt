package pl.jakubokrasa.bikeroutes.features.routerecording.ui.model

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

data class PointDisplayable(
    val pointId: Long,
    val routeId: Long,
    val geoPoint: GeoPoint
) {
    constructor(point: Point) : this (
        pointId = point.pointId,
        routeId = point.routeId,
        geoPoint = point.geoPoint
    )
}