package pl.jakubokrasa.bikeroutes.features.map.data.remote.model

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class RouteResponse(
    val routeId: String,
    val createdAt: Long,
    val userId: String,
    val name: String,
    val description: String,
    val sharingType: SharingType,
    val distance: Int,
    val rideTimeMinutes: Int,
) {

    constructor() : this("", 0,"", "", "", pl.jakubokrasa.bikeroutes.core.util.enums.SharingType.PRIVATE, 0, 0)

    constructor(route: Route) : this(route.routeId, route.createdAt, route.userId, route.name, route.description, route.sharingType, route.distance, route.rideTimeMinutes)

    fun toRoute(): Route {
        return Route(
            routeId = this.routeId,
            createdAt = this.createdAt,
            userId = this.userId,
            name = this.name,
            description = this.description,
            distance = this.distance,
            sharingType = this.sharingType,
            rideTimeMinutes = this.rideTimeMinutes
        )
    }
}