package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuth
import pl.jakubokrasa.bikeroutes.core.user.sharingType
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class SaveRouteUseCase(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val userAuth: UserAuth): UseCase<Unit, DataSaveRoute>() {
    override suspend fun action(params: DataSaveRoute) {
        val points = localRepository.getPoints2().map { it.toPoint() }
        val route = Route(
            "",
            userAuth.getCurrentUserId(),
            params.name,
            params.description,
            params.distance,
            sharingType.PRIVATE,
        )
        remoteRepository.addRoute(route, points)
        localRepository.deletePoints()
    }

}

data class DataSaveRoute (
    val name: String,
    val description: String,
    val distance: Int
)