package pl.jakubokrasa.bikeroutes.core.user.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse

@IgnoreExtraProperties
data class UserResponse(
    val routes: List<RouteResponse>,
) {

}
