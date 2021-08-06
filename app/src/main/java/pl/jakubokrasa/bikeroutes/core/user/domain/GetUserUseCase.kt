package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.model.User

class GetUserUseCase(private val repository: UserRepository): UseCase<User, String>() {
    override suspend fun action(params: String): User {
        return repository.getUser(params)
    }
}