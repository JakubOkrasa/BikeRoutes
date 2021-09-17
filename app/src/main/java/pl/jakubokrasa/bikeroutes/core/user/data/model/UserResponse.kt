package pl.jakubokrasa.bikeroutes.core.user.data.model

import pl.jakubokrasa.bikeroutes.core.user.domain.model.User

data class UserResponse(
    val displayName: String
) {

    constructor(): this("") // for firestore

    fun toUser(): User {
        return User(this.displayName)
    }
}
