package pl.jakubokrasa.bikeroutes.features.common.routes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.model.Route

class RemoveRouteUseCase(private val repository: RouteRepository): UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
        repository.deleteRoute(params)
    }
}