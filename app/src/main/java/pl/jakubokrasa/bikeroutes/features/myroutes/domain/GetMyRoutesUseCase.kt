package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

class GetMyRoutesUseCase(private val routeRepository: RouteRepository):
    UseCase<List<Route>, Unit>() {

    override suspend fun action(params: Unit) = routeRepository.getMyRoutes()
}