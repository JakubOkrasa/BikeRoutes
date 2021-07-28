package pl.jakubokrasa.bikeroutes.core.user.domain

interface UserRepository {

    suspend fun createUser(uid: String)

    suspend fun deleteUser(uid: String)
}

