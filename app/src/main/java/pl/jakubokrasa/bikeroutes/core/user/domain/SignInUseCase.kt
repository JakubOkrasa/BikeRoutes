package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase

class SignInUseCase(private val auth: UserAuth): UseCase<Unit, DataSignIn>() {
    override suspend fun action(params: DataSignIn) {
        auth.signIn(params.email, params.password)
    }

}

data class DataSignIn(
    val email: String,
    val password: String
)