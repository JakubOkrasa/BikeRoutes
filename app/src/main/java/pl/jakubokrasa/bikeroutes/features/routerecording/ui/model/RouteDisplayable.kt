package pl.jakubokrasa.bikeroutes.features.routerecording.ui.model

import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

data class RouteDisplayable(
    val current: Boolean,
    var points: List<Point>
) {
    fun toRoute(): Route {
        return Route(this.current, this.points)
    }

    constructor(route: Route) : this (
        current = route.current,
        points = route.points
        )
}