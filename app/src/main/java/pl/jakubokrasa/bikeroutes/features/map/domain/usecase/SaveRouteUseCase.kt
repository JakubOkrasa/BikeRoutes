package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import java.util.concurrent.TimeUnit

class SaveRouteUseCase(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository,
    private val userAuth: UserAuth
): UseCase<Unit, DataSaveRoute>() {
    override suspend fun action(params: DataSaveRoute) {
        val points = localRepository.getPoints2()
        if(points.isNotEmpty()) {
            val route = Route(
                "",
                System.currentTimeMillis(),
                userAuth.getCurrentUserId(),
                params.name,
                params.description,
                params.distance,
                params.sharingType,
                getRideTimeMinutes(points),
            )
            remoteRepository.addRoute(route, points)
            localRepository.deletePoints()
        }
    }

    private fun getRideTimeMinutes(points: List<Point>): Int {
        val rideTime = points[points.size - 1].createdAt - points[0].createdAt
        return TimeUnit.MILLISECONDS.toMinutes(rideTime).toInt()
    }

}

data class DataSaveRoute (
    val name: String,
    val description: String,
    val distance: Int,
    val sharingType: SharingType,
)