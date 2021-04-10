package pl.jakubokrasa.bikeroutes.core.user.domain

import pl.jakubokrasa.bikeroutes.core.base.UseCase

class CreateUserUseCase(private val userRepository: UserRepository): UseCase<Unit, String>() {
    override suspend fun action(params: String) {
// TODO: 4/9/2021 read it https://proandroiddev.com/suspending-firebase-realtime-database-with-kotlin-coroutines-76b4651bc0e8 

        userRepository.createUser(uid, params)
    }
}