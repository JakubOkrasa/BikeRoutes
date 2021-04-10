package pl.jakubokrasa.bikeroutes.core.user.domain
interface UserRepository {

    fun createUser(uid: String, email: String)
}

