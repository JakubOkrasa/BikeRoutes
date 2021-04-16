package pl.jakubokrasa.bikeroutes.features.map.domain

import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteRepository

class GetCurrentRouteUseCase(private val routeRepository: RouteRepository): UseCase<Route, Unit>() {
    override suspend fun action(params: Unit) = routeRepository.getCurrentRoute()
}