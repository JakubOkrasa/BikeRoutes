package pl.jakubokrasa.bikeroutes.core.user.domain

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.base.UseCase

class CreateUserUseCase(private val userRepository: UserRepository,
                        private val userAuth: UserAuth): UseCase<Unit, CreateUserData>() {
    override suspend fun action(params: CreateUserData) {
        userAuth.createUser(params.email, params.password)

    }
//            auth.createUserWithEmailAndPassword(params.email, params.password)
//                .await()
//                .user
//                ?.let {
//                   userRepository.createUser(it.uid)
//                }
//        }
}

data class CreateUserData(
    val email: String,
    val password: String
)