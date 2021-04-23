package pl.jakubokrasa.bikeroutes.features.map.data.remote.model

import pl.jakubokrasa.bikeroutes.core.user.sharingType

data class RouteResponse(
    val id: Long,
    val name: String,
    val description: String,
    val sharingType: sharingType,
    val distance: String,
    val points: List<PointResponse>,
//    val photos: List<PhotoResponse>,
    //val thumbnail image
) {}