package pl.jakubokrasa.bikeroutes.features.routerecording.ui.model

import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

data class RouteDisplayable(
    val routeId: Long,
    val current: Boolean,
    var points: List<Point>
) {
    fun toRoute(): Route {
        return Route(this.routeId, this.current, this.points)
    }

    constructor(route: Route) : this (
        routeId = route.routeId,
        current = route.current,
        points = route.points
            )
}