package pl.jakubokrasa.bikeroutes.features.routerecording.ui.model

import pl.jakubokrasa.bikeroutes.core.user.sharingType
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import java.io.Serializable

data class RouteWithPointsDisplayable (
    val name: String,
    val description: String,
    val current: Boolean,
    val distance: Int,
    val sharingType: sharingType,
    var points: List<Point>
): Serializable {

    fun toRoute(): Route {
        return Route(
            name = name,
            description = description,
            current = current,
            distance = distance,
            sharingType = sharingType,
            points = points)
    }

    constructor(route: Route) : this (
        name = route.name,
        description = route.description,
        current = route.current,
        distance = route.distance,
        sharingType = route.sharingType,
        points = route.points)
}