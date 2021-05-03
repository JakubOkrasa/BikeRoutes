package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.user.sharingType
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class SaveRouteUseCase(
    private val pointRepository: PointRepository,
    private val routeRepository: RouteRepository): UseCase<Unit, DataSaveRoute>() {
    override suspend fun action(params: DataSaveRoute) {
        val points = pointRepository.getPoints().value
        routeRepository.addRoute(Route(
            0,
            params.name,
            params.description,
            params.distance,
            sharingType.PRIVATE,
            points?.map { it.toPoint() } ?: throw Exception("SaveRouteUseCase: points is null")
        ))
        pointRepository.deletePoints()
    }

}

data class DataSaveRoute (
    val name: String,
    val description: String,
    val distance: Int
)