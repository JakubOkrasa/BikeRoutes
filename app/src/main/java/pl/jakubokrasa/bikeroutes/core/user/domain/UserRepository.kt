package pl.jakubokrasa.bikeroutes.core.user.domain
interface UserRepository {

    suspend fun createUser(params: CreateUserData)
}

