package pl.jakubokrasa.bikeroutes.core.user.data.remote.model

import pl.jakubokrasa.bikeroutes.core.user.domain.model.User

data class UserResponse(
    val displayName: String
) {

    constructor(): this("")

    fun toUser(): User {
        return User(this.displayName)
    }
}
