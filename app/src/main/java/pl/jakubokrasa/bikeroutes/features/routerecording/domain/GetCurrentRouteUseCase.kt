package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.CurrentRouteRepository

class GetCurrentRouteUseCase(private val repository: CurrentRouteRepository): UseCase<CurrentRoutePoint, Unit>() {
    override suspend fun action(params: Unit) = repository.getCurrentRoute()
}