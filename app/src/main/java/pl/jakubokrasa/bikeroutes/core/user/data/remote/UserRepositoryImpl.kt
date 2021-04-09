package pl.jakubokrasa.bikeroutes.core.user.data.remote

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import pl.jakubokrasa.bikeroutes.core.user.domain.UserRepository

class UserRepositoryImpl(private val dbRef: DatabaseReference): UserRepository {

    override fun getUsers() {
//        dbRef.
    }


}