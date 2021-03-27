package pl.jakubokrasa.bikeroutes.features.routerecording.presentation.model

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point

data class PointDisplayable(
    val pointId: Long,
    val geoPoint: GeoPoint
) {
    constructor(point: Point) : this (
        pointId = point.pointId,
        geoPoint = point.geoPoint
    )
}