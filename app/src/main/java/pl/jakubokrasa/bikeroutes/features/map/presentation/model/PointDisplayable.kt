package pl.jakubokrasa.bikeroutes.features.map.presentation.model

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

data class PointDisplayable(
    val pointId: Long,
    val geoPoint: GeoPoint
) {
    constructor(point: Point) : this (
        pointId = point.pointId,
        geoPoint = point.geoPoint
    )
}