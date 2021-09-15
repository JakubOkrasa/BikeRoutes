package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase

class IsUserSignedInUseCase(private val auth: UserAuth): UseCase<Boolean, Unit>() {
    override suspend fun action(params: Unit) = auth.isUserSignedIn()
}