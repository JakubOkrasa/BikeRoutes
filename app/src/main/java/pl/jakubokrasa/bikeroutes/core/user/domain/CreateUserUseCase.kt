package pl.jakubokrasa.bikeroutes.core.user.domain

import android.content.Context
import com.google.android.gms.common.UserRecoverableException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.user.data.remote.model.UserResponse

class CreateUserUseCase(private val userRepository: UserRepository,
                        private val auth: FirebaseAuth): UseCase<Unit, CreateUserData>() {
    override suspend fun action(params: CreateUserData) {
        auth.createUserWithEmailAndPassword(params.email, params.password)
        //betterprogramming robi tu onCompleteListener. Czy to mogę robić w VM? Tam mam coroutinowe onSuccess i onFailure
        userRepository.createUser(params)
    }
}

data class CreateUserData(
    val email: String,
    val password: String
) {
}