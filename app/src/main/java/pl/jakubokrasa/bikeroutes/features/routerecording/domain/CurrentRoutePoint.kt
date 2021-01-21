package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import org.osmdroid.util.GeoPoint

data class CurrentRoutePoint(
    val id: Long,
    val point: GeoPoint
) {

}