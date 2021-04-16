package pl.jakubokrasa.bikeroutes.core.user.domain

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference

interface UserRepository {

    suspend fun createUser(uid: String)

    suspend fun deleteUser(uid: String)
}

