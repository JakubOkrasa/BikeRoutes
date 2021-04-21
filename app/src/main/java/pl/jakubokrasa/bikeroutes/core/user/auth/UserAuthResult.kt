package pl.jakubokrasa.bikeroutes.core.user.auth

data class UserAuthResult(
    var success: Boolean = false,
    var uid: String? = null,
    var message: String? = null
)