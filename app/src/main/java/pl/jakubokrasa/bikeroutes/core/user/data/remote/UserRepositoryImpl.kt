package pl.jakubokrasa.bikeroutes.core.user.data.remote

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import pl.jakubokrasa.bikeroutes.core.user.data.remote.model.UserResponse
import pl.jakubokrasa.bikeroutes.core.user.domain.CreateUserData
import pl.jakubokrasa.bikeroutes.core.user.domain.UserRepository
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse

class UserRepositoryImpl(private val firestore: FirebaseFirestore): UserRepository {
    override suspend fun createUser(params: CreateUserData) {
        val userResponse = UserResponse(params.email, params.password, ArrayList())
        firestore.collection("users").add(userResponse)

    }


}