package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.user.domain.model.User

interface UserRepository {

    suspend fun createUser(uid: String, displayName: String)

    suspend fun deleteUser(uid: String)

    suspend fun getUser(uid: String): User
}

