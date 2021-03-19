package pl.jakubokrasa.bikeroutes.features.routerecording.ui.model

import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

data class RouteWithPointsDisplayable(
    val name: String,
    val distance: Int,
    val current: Boolean,
    var points: List<Point>
) {
    fun toRoute(): Route {
        return Route(this.name, this.distance, this.current, this.points)
    }

    constructor(route: Route) : this (
        name = route.name,
        distance = route.distance,
        current = route.current,
        points = route.points
        )
}