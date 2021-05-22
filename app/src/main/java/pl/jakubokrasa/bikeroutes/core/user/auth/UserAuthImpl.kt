package pl.jakubokrasa.bikeroutes.core.user.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import java.lang.Exception

class UserAuthImpl(private val auth: FirebaseAuth): UserAuth {
    override suspend fun createUser(email: String, password: String): UserAuthResult {
        val userAuthResult = UserAuthResult()
        val exception: Exception?
        val task = auth.createUserWithEmailAndPassword(email, password)
            //Kotlin nie tworzy tu nowego obiektu klasy Exception, tylko przypisuje do
            // referencji, dlatego przy sprawdzaniu czy == null ten obiekt będzie
            // miał dane z czasu PO wykonaniu funkcji await()
            .also { exception = it.exception }
            .await()

        userAuthResult.success = exception == null
        userAuthResult.uid = task.user?.uid
        return userAuthResult
    }

    override suspend fun deleteCurrentUser(): UserAuthResult {
        val userAuthResult = UserAuthResult()
        val exception: Exception?
        val user = auth.currentUser
        if(user!=null) {
            userAuthResult.uid = user.uid
            auth.currentUser!!.delete()
                .also { exception = it.exception }
                .await()
        } else {
            exception = Exception("Error while deleting account: user is null")
        }

        userAuthResult.success = exception == null
        return userAuthResult
    }

    override suspend fun getCurrentUserId(): String {
        val user = auth.currentUser
        user?.let {
            return it.uid
        }
        throw Exception("UserAuth: no current user" )
    }

    override suspend fun isUserSignedIn(): Boolean {
        auth.currentUser?.let {
            return true
        }
        return false
    }

    override suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun logOut() {
        auth.signOut()
    }

}