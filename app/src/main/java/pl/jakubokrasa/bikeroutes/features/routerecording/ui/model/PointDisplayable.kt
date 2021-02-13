package pl.jakubokrasa.bikeroutes.features.routerecording.ui.model

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

data class PointDisplayable(
    val pointId: Long,
    val geoPoint: GeoPoint
) {
    constructor(point: Point) : this (
        pointId = point.pointId,
        geoPoint = point.geoPoint
    )
}