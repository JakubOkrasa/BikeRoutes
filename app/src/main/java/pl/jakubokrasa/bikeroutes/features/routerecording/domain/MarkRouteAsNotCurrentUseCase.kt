package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteRepository

class MarkRouteAsNotCurrentUseCase(private val routeRepository: RouteRepository) : UseCase<Unit, Unit>() {
    override suspend fun action(params: Unit) {
        routeRepository.markRouteAsNotCurrent()
    }
}