package pl.jakubokrasa.bikeroutes.features.map.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

data class RouteWithPointsCached(
    @Embedded val route: RouteCached,
    @Relation(
        parentColumn = "routeId",
        entityColumn = "routeId"
    )
    var points: List<PointCached>
) {
    fun toRoute() = Route(
        route.routeId,
        route.name,
        route.description,
        route.current,
        route.distance,
        route.sharingType,
        points.map {
            Point(it.pointId, it.geoPoint, it.createdAt)
        }
    )
}