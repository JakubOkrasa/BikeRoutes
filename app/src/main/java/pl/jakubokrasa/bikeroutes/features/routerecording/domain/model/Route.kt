package pl.jakubokrasa.bikeroutes.features.routerecording.domain.model

import org.osmdroid.util.GeoPoint

data class Route(
    val name: String,
    val distance: Int,
    val current: Boolean,
    var points: List<Point>
) {}