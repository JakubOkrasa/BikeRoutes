package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuth

class DeleteCurrentUserUseCase(
    private val userAuth: UserAuth,
    private val userRepository: UserRepository
    ): UseCase<Unit, Unit>() {
    override suspend fun action(params: Unit) {
        val result = userAuth.deleteCurrentUser()
        if(result.success)
            userRepository.deleteUser(result.uid!!)
    }
}