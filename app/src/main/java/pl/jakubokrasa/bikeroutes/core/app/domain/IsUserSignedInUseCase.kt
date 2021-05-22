package pl.jakubokrasa.bikeroutes.core.app.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth

class IsUserSignedInUseCase(private val auth: UserAuth): UseCase<Boolean, Unit>() {
    override suspend fun action(params: Unit): Boolean {
        return auth.isUserSignedIn()
    }

}