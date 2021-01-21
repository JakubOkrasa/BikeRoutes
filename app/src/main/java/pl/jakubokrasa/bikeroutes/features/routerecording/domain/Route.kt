package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import org.osmdroid.util.GeoPoint

data class Route(
    private var points: List<GeoPoint>
) {

}