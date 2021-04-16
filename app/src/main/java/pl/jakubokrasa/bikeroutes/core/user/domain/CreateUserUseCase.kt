package pl.jakubokrasa.bikeroutes.core.user.domain

import android.content.Context
import com.google.android.gms.common.UserRecoverableException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.user.data.remote.model.UserResponse

class CreateUserUseCase(private val userRepository: UserRepository,
                        private val auth: FirebaseAuth): UseCase<String, CreateUserData>() {
    override suspend fun action(params: CreateUserData): String {
        var uid = ""

            auth.createUserWithEmailAndPassword(params.email, params.password)
                .await()
                .user
                ?.let {
                   uid = it.uid
                }
        return uid
        }




//        var task: Task<AuthResult>? = null
//        auth.createUserWithEmailAndPassword(params.email, params.password)
//            .also { task = it }
//            .addOnSuccessListener() {
//                task?.result?.user?.let {
//                    userRepository.createUser(it.uid, params)
//            }
//            .also {
//
//            }
//        }

        // TODO: 4/16/2021 await func


        //maybe create distinct usecase for saving to firebase

//        auth.createUserWithEmailAndPassword(params.email, params.password)
//            .also {
//                task ->
//            task.result.user?.let {
//                userRepository.createUser(it.uid, params)
//            }
//        }



//        auth.createUserWithEmailAndPassword(params.email, params.password).await()
//        userRepository.createUser(params)
}

data class CreateUserData(
    val email: String,
    val password: String
) {
}