package pl.jakubokrasa.bikeroutes.core.user.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.user.data.remote.model.UserResponse
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.UserRepository

class UserRepositoryImpl(private val firestore: FirebaseFirestore): UserRepository {
    override suspend fun createUser(params: CreateUserData) {
        val userResponse = UserResponse(params.email, params.password, ArrayList())
        firestore.collection("users").add(userResponse).await()

    }


}