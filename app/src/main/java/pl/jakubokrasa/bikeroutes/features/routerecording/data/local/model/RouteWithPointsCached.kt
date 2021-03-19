package pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

data class RouteWithPointsCached(
    @Embedded val route: RouteCached,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "routeId"
    )
    val points: List<PointCached>
) {
    fun toRoute() = Route(
        route.name,
        route.distance,
        route.current,
        points.map {
            Point(it.pointId, it.geoPoint)
        }
    )
}