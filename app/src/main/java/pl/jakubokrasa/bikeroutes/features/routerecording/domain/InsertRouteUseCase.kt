package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteRepository

class InsertRouteUseCase(private val routeRepository: RouteRepository):UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
        routeRepository.insertRoute(params)
    }
}