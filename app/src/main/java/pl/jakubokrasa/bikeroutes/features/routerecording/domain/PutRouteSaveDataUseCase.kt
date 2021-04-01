package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteRepository

class PutRouteSaveDataUseCase(private val routeRepository: RouteRepository): UseCase<Unit, DataRouteSave>() {
    override suspend fun action(params: DataRouteSave) {
        val id = routeRepository.getCurrentRouteId()
//        routeRepository.updateRouteName(id, params.name)
        routeRepository.updateRouteDescription(id, params.description)
        routeRepository.updateRouteDistance(id, params.distance)
    }
}

data class DataRouteSave (
    val name: String,
    val description: String,
    val distance: Int
    )