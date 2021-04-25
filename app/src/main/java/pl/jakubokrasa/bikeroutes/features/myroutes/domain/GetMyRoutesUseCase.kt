package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteRepository

class GetMyRoutesUseCase(private val routeRepository: RouteRepository):
    UseCase<List<Route>, Unit>() {

    override suspend fun action(params: Unit) = TODO()  // routeRepository.getMyRoutes()
}