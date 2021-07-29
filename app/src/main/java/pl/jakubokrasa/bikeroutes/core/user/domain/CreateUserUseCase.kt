package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuthResult

class CreateUserUseCase(private val userAuth: UserAuth,
                        private val userRepository: UserRepository)
    : UseCase<UserAuthResult, CreateUserData>() {
    override suspend fun action(params: CreateUserData): UserAuthResult {
        val result = userAuth.createUser(params.email, params.password)
        if (result.success)
           userRepository.createUser(result.uid!!, params.displayName)
        return result
    }
}

data class CreateUserData(
    val email: String,
    val password: String,
    val displayName: String
)