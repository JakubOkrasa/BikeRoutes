package pl.jakubokrasa.bikeroutes.core.user.domain

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuth
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuthResult

class UserAuthImpl(private val auth: FirebaseAuth): UserAuth {
    override suspend fun createUser(email: String, password: String): UserAuthResult {
        val userAuthResult = UserAuthResult()
        auth.createUserWithEmailAndPassword(email, password)
            .also {
                userAuthResult.success = it.isSuccessful
                userAuthResult.uid = it.result?.user?.uid
                userAuthResult.message = it.exception?.message
            }.await()

        return userAuthResult
    }
}