package pl.jakubokrasa.bikeroutes.features.routes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routes.domain.model.Route

class UpdateRouteUseCase(private val repository: RouteRepository): UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
        repository.updateRoute(params)
    }

}