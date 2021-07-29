package pl.jakubokrasa.bikeroutes.core.user.presentation.model

import pl.jakubokrasa.bikeroutes.core.user.domain.model.User

data class UserDisplayable(
    val displayName: String
) {
    constructor(user: User): this(user.displayName)
}