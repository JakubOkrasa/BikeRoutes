package pl.jakubokrasa.bikeroutes.core.user.domain

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class UserAuthImpl(private val auth: FirebaseAuth): UserAuth {
    override suspend fun createUser(email: String, password: String): Pair<String?, String?> {
        var exception: Exception?
        val user = auth.createUserWithEmailAndPassword(email, password)
            .also { exception = it.exception } // to chyba przypisze się zanim rejestracja się ukończy, a ja chcę przypisać po zakończeniu
            .await()

        return Pair(user?.let { it.user?.uid }, exception?.message)


    }

}