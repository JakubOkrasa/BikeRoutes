package pl.jakubokrasa.bikeroutes.features.map.data.remote.model

import com.google.firebase.Timestamp
import pl.jakubokrasa.bikeroutes.core.user.sharingType
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class RouteResponse(
    val routeId: String,
    val createdAt: Long,
    val userId: String,
    val name: String,
    val description: String,
    val sharingType: sharingType,
    val distance: Int,
    val rideTimeMinutes: Int,
) {

    constructor() : this("", 0,"", "", "", pl.jakubokrasa.bikeroutes.core.user.sharingType.PRIVATE, 0, 0)

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