package pl.jakubokrasa.bikeroutes.core.user.data.remote

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pl.jakubokrasa.bikeroutes.core.user.data.remote.model.UserResponse
import pl.jakubokrasa.bikeroutes.core.user.domain.UserRepository
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse

class UserRepositoryImpl(private val dbRef: DatabaseReference): UserRepository {
    override fun createUser(uid: String, email: String) {
        val user = UserResponse(email, ArrayList())
        dbRef.child(uid).setValue(user)
    }


}