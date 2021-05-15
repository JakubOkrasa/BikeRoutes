package pl.jakubokrasa.bikeroutes.features.map.domain.model

import pl.jakubokrasa.bikeroutes.core.user.sharingType

data class Route(
    val routeId: String,
    val createdAt: Long,
    val userId: String,
    val name: String,
    val description: String,
    val distance: Int,
    val sharingType: sharingType,
    val rideTimeMinutes: Int,
) {}