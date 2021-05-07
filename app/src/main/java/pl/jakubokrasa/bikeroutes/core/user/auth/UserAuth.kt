package pl.jakubokrasa.bikeroutes.core.user.auth


interface UserAuth {
    suspend fun createUser(email: String, password: String): UserAuthResult

    suspend fun deleteCurrentUser(): UserAuthResult

    suspend fun getCurrentUserId(): String
}