package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuth

class CreateUserUseCase(private val userAuth: UserAuth,
                        private val userRepository: UserRepository)
    : UseCase<Unit, CreateUserData>() {
    override suspend fun action(params: CreateUserData) {
        val result = userAuth.createUser(params.email, params.password)
        if (result.success)
           userRepository.createUser(result.uid!!)
    }
}

data class CreateUserData(
    val email: String,
    val password: String
)