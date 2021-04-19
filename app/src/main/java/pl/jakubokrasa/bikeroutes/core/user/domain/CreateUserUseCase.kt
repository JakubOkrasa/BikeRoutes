package pl.jakubokrasa.bikeroutes.core.user.domain

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.base.UseCase

class CreateUserUseCase(private val userAuth: UserAuth,
                        private val userRepository: UserRepository): UseCase<String?, CreateUserData>() {
    override suspend fun action(params: CreateUserData): String? {
        val result = userAuth.createUser(params.email, params.password)
        if (result.first != null) {
            userRepository.createUser(result.first!!)
        } else {
            return result.second
        }
        return null
    }
}

data class CreateUserData(
    val email: String,
    val password: String
)