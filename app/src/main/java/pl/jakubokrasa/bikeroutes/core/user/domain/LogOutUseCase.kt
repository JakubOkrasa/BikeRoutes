package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase

class LogOutUseCase(private val auth: UserAuth): UseCase<Unit, Unit>() {
    override suspend fun action(params: Unit) {
        auth.logOut()
    }
}