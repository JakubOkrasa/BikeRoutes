package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.CurrentRoutePointRepository

class GetCurrentRouteUseCase(private val pointRepository: CurrentRoutePointRepository): UseCase<CurrentRoutePoint, Unit>() {
    override suspend fun action(params: Unit) = pointRepository.getCurrentRoute()
}