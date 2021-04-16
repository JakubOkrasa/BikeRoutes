package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.UseCase

class SaveUserToFirestoreUseCase(private val userRepository: UserRepository): UseCase<Unit, String>() {
    override suspend fun action(params: String) {
        userRepository.createUser(params)
    }

}