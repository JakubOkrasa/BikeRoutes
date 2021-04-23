package pl.jakubokrasa.bikeroutes.core.user.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.user.data.remote.model.UserResponse
import pl.jakubokrasa.bikeroutes.core.user.domain.UserRepository

class UserRepositoryImpl(private val firestore: FirebaseFirestore): UserRepository {

    override suspend fun createUser(uid: String) {
        val userResponse = UserResponse(ArrayList())
        firestore.collection("users").document(uid).set(userResponse).await()
    }

    override suspend fun deleteUser(uid: String) {
        firestore.collection("users").document(uid).delete()
    }


}