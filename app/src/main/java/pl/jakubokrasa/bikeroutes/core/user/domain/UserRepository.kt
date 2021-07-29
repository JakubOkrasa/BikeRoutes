package pl.jakubokrasa.bikeroutes.core.user.domain

interface UserRepository {

    suspend fun createUser(uid: String, displayName: String)

    suspend fun deleteUser(uid: String)
}

