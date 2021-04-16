package pl.jakubokrasa.bikeroutes.core.user.domain

import android.content.Context
import com.google.android.gms.common.UserRecoverableException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.user.data.remote.model.UserResponse

class CreateUserUseCase(private val userRepository: UserRepository,
                        private val auth: FirebaseAuth): UseCase<Unit, CreateUserData>() {
    override suspend fun action(params: CreateUserData) {
        auth.createUserWithEmailAndPassword(params.email, params.password).await()
        userRepository.createUser(params)
    }

    // TODO: 4/16/2021 use it with also fun https://stackoverflow.com/questions/49996226/how-to-add-user-uid-in-the-place-of-firestore-document-id
}

data class CreateUserData(
    val email: String,
    val password: String
) {
}