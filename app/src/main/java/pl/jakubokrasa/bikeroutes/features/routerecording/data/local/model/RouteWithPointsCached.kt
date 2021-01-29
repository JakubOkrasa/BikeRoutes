package pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

data class RouteWithPointsCached(
    @Embedded val route: Route,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "pointId"
    )
    val points: List<Point>
) {
//    fun toRouteWithPoints() = RouteWithPoints(
//        route.routeId,
//        route.current,
//        points
//    )
}