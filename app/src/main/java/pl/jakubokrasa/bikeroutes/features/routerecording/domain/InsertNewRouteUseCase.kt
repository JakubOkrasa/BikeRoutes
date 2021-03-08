package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

class InsertNewRouteUseCase(private val routeRepository: RouteRepository):UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
        routeRepository.insertRoute(params)
    }
}