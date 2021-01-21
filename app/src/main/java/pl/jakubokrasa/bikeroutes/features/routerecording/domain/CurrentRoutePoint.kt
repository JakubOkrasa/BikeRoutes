package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import org.osmdroid.util.GeoPoint

data class CurrentRoutePoint(
    private val id: Long,
    private val point: GeoPoint
) {

}