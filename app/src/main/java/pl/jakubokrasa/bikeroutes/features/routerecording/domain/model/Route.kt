package pl.jakubokrasa.bikeroutes.features.routerecording.domain.model

import org.osmdroid.util.GeoPoint

data class Route(
    val routeId: Long,
    val current: Boolean,
    var points: List<Point>
) {}