package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import android.util.Log
import androidx.lifecycle.LiveData
import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuth
import pl.jakubokrasa.bikeroutes.core.user.sharingType
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class SaveRouteUseCase(
    private val pointRepository: PointRepository,
    private val routeRepository: RouteRepository,
    private val userAuth: UserAuth): UseCase<Unit, DataSaveRoute>() {
    override suspend fun action(params: DataSaveRoute) {
        val points = pointRepository.getPoints2()
        val route = Route(
            userAuth.getCurrentUserId(),
            params.name,
            params.description,
            params.distance,
            sharingType.PRIVATE,
            points.map { it.toPoint() }
        )
        routeRepository.addRoute(route)
        pointRepository.deletePoints()
    }

}

data class DataSaveRoute (
    val name: String,
    val description: String,
    val distance: Int
)