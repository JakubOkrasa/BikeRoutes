package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase

class ResetPasswordUseCase(private val auth: UserAuth): UseCase<Unit, String>() {
    override suspend fun action(params: String) {
        auth.resetPassword(params)
    }

}