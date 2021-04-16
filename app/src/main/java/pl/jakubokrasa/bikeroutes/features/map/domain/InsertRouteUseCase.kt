package pl.jakubokrasa.bikeroutes.features.map.domain

import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteRepository

class InsertRouteUseCase(private val routeRepository: RouteRepository): UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
        routeRepository.insertRoute(params)
    }
}