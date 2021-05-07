package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository

class DeleteRouteUseCase(private val repository: PointRepository): UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
//        repository.deleteRoute(params)
    }
}