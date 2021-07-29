package pl.jakubokrasa.bikeroutes.core.user.data.remote.model

import com.google.firebase.database.IgnoreExtraProperties
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse

data class UserResponse(
    val displayName: String
) {

    constructor(): this("")
}
