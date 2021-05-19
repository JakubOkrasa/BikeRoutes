package pl.jakubokrasa.bikeroutes.features.map.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.jakubokrasa.bikeroutes.core.util.enums.sharingType
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

@Parcelize
data class RouteDisplayable (
    val routeId: String,
    val createdAt: Long,
    val userId: String,
    val name: String,
    val description: String,
    val distance: Int,
    val sharingType: sharingType,
    val rideTimeMinutes: Int,
    ): Parcelable {

    fun toRoute(): Route {
        return Route(
            routeId = routeId,
            createdAt = createdAt,
            userId = userId,
            name = name,
            description = description,
            distance = distance,
            sharingType = sharingType,
            rideTimeMinutes = rideTimeMinutes,)
    }

    constructor(route: Route) : this (
        routeId = route.routeId,
        createdAt = route.createdAt,
        userId = route.userId,
        name = route.name,
        description = route.description,
        distance = route.distance,
        sharingType = route.sharingType,
        rideTimeMinutes = route.rideTimeMinutes,
    )
}