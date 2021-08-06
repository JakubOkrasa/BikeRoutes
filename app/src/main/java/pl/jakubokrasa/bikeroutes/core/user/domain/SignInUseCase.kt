package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuthResult

class SignInUseCase(private val auth: UserAuth): UseCase<UserAuthResult, DataSignIn>() {
    override suspend fun action(params: DataSignIn): UserAuthResult {
        return auth.signIn(params.email, params.password)
    }

}

data class DataSignIn(
    val email: String,
    val password: String
)