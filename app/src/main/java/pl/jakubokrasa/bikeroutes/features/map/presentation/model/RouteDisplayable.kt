package pl.jakubokrasa.bikeroutes.features.map.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.jakubokrasa.bikeroutes.core.user.sharingType
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import java.io.Serializable

@Parcelize
data class RouteDisplayable (
//    val routeId: Long,
    val userId: String,
    val name: String,
    val description: String,
    val distance: Int,
    val sharingType: sharingType,
    var points: List<Point>
): Parcelable {

    fun toRoute(): Route {
        return Route(
//            routeId = routeId,
            userId = userId,
            name = name,
            description = description,
            distance = distance,
            sharingType = sharingType,
            points = points)
    }

    constructor(route: Route) : this (
//        routeId = route.routeId,
        userId = route.userId,
        name = route.name,
        description = route.description,
        distance = route.distance,
        sharingType = route.sharingType,
        points = route.points)
}