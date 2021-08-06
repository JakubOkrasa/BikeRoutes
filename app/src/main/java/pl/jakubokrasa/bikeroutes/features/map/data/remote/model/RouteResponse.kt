package pl.jakubokrasa.bikeroutes.features.map.data.remote.model

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.domain.BoundingBoxData
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
    val avgSpeedKmPerH: Int,
    val boundingBoxData: BoundingBoxData,
    val createdBy: String
) {

    constructor() : this("", 0,"", "", "", pl.jakubokrasa.bikeroutes.core.util.enums.SharingType.PRIVATE, //for firestore
        0, 0, 0, BoundingBoxData(0.0, 0.0,0.0,0.0), "")

    constructor(route: Route) : this(route.routeId, route.createdAt, route.userId, route.name, route.description, route.sharingType,
        route.distance, route.rideTimeMinutes, route.avgSpeedKmPerH, route.boundingBoxData, route.createdBy)

    fun toRoute(): Route {
        return Route(
            routeId = this.routeId,
            createdAt = this.createdAt,
            userId = this.userId,
            name = this.name,
            description = this.description,
            distance = this.distance,
            sharingType = this.sharingType,
            rideTimeMinutes = this.rideTimeMinutes,
            avgSpeedKmPerH = this.avgSpeedKmPerH,
            boundingBoxData = this.boundingBoxData,
            createdBy = this.createdBy
        )
    }
}