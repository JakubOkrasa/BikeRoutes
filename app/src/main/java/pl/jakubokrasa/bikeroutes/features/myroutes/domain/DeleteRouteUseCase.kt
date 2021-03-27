package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteRepository

class DeleteRouteUseCase(private val repository: RouteRepository): UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
        repository.deleteRoute(params)
    }
}