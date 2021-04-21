package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuth

class CreateUserUseCase(private val userAuth: UserAuth,
                        private val userRepository: UserRepository): UseCase<String?, CreateUserData>() {
    override suspend fun action(params: CreateUserData): String? {
        val result = userAuth.createUser(params.email, params.password)
        if (result.success) {
            userRepository.createUser(result.uid!!)
        } else {
            return result.message
        }
        return null
    }
}

data class CreateUserData(
    val email: String,
    val password: String
)