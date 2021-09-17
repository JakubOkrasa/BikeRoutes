package pl.jakubokrasa.bikeroutes.features.common.routes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.model.Route

class UpdateRouteUseCase(private val repository: RouteRepository): UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
        repository.updateRoute(params)
    }

}