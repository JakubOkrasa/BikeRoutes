package pl.jakubokrasa.bikeroutes.features.map.presentation.model

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import java.io.Serializable

data class PointDisplayable(
    val pointId: Long,
    val geoPoint: GeoPoint
): Serializable {
    constructor(point: Point) : this (
        pointId = point.pointId,
        geoPoint = GeoPoint(
            point.geoPointData.latitude, point.geoPointData.longitude)
    )

    fun toPointNoCreatedAt(): Point {
        return Point(
            this.pointId,
            GeoPointData(geoPoint.latitude, geoPoint.longitude),
            0,
        )
    }
}