package pl.jakubokrasa.bikeroutes.core.user.domain

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.base.UseCase

class CreateUserUseCase(private val userAuth: UserAuth,
                        private val userRepository: UserRepository): UseCase<Unit, CreateUserData>() {
    override suspend fun action(params: CreateUserData) {
        val uid = userAuth.createUser(params.email, params.password)
        uid?.let { userRepository.createUser(it) }
    }
}

data class CreateUserData(
    val email: String,
    val password: String
)