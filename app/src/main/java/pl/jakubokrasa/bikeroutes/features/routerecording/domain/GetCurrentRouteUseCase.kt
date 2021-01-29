package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.CurrentRoutePointRepository
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

class GetCurrentRouteUseCase(private val pointRepository: CurrentRoutePointRepository): UseCase<Route, Unit>() {
    override suspend fun action(params: Unit) = pointRepository.getCurrentRoute()
}