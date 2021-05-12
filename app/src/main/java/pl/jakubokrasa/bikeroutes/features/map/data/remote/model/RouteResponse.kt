package pl.jakubokrasa.bikeroutes.features.map.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.jakubokrasa.bikeroutes.core.user.sharingType
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class RouteResponse(
//    val id: String,
    val userId: String,
    val name: String,
    val description: String,
    val sharingType: sharingType,
    val distance: Int,
//    val points: List<PointResponse>,
//    val photos: List<PhotoResponse>,
    //val thumbnail image
) {

    constructor() : this("", "", "", pl.jakubokrasa.bikeroutes.core.user.sharingType.PRIVATE, 0)

//    fun toRoute(routeResponse: RouteResponse) {
//        return Route(
//            userId = routeResponse.userId,
//            name = routeResponse.name,
//            description = routeResponse.description,
//            distance = routeResponse.distance,
//            sharingType = routeResponse.sharingType,
//            // todo brak points
//        )
//    }
}