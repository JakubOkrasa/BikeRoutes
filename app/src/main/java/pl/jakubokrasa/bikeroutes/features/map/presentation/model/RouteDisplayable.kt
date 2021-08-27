package pl.jakubokrasa.bikeroutes.features.map.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

@Parcelize
data class RouteDisplayable (
    val routeId: String,
    val createdAt: Long,
    val userId: String,
    var name: String,
    var description: String,
    val distance: Int,
    var sharingType: SharingType,
    val rideTimeMinutes: Int,
    val avgSpeedKmPerH: Int,
    val boundingBoxData: BoundingBoxData,
    val createdBy: String
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
            rideTimeMinutes = rideTimeMinutes,
            avgSpeedKmPerH = avgSpeedKmPerH,
            boundingBoxData = boundingBoxData,
            createdBy = createdBy)
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
        avgSpeedKmPerH = route.avgSpeedKmPerH,
        boundingBoxData = route.boundingBoxData,
        createdBy = route.createdBy
    )
}