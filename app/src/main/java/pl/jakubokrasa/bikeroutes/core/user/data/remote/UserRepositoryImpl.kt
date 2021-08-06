package pl.jakubokrasa.bikeroutes.core.user.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.user.data.remote.model.UserResponse
import pl.jakubokrasa.bikeroutes.core.user.domain.UserRepository
import pl.jakubokrasa.bikeroutes.core.user.domain.model.User

class UserRepositoryImpl(private val firestore: FirebaseFirestore): UserRepository {

    override suspend fun createUser(uid: String, displayName: String) {
        val userResponse = UserResponse(displayName)
        firestore.collection("users").document(uid).set(userResponse).await()
    }

    override suspend fun deleteUser(uid: String) {
        firestore.collection("users").document(uid).delete()
    }

    override suspend fun getUser(uid: String): User {
        return firestore.collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(UserResponse::class.java)
            ?.toUser() ?: User("unknown-user")
    }


}