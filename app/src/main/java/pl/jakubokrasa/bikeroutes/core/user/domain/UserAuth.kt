package pl.jakubokrasa.bikeroutes.core.user.domain

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface UserAuth {
    suspend fun createUser(email: String, password: String): Pair<String?, String?>

}