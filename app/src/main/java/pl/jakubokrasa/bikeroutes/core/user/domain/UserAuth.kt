package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuthResult


interface UserAuth {
    suspend fun createUser(email: String, password: String): UserAuthResult

    suspend fun deleteCurrentUser(): UserAuthResult

    suspend fun getCurrentUserId(): String
    suspend fun isUserSignedIn(): Boolean

    suspend fun resetPassword(email: String)

    suspend fun signIn(email: String, password: String)

    suspend fun logOut()
}