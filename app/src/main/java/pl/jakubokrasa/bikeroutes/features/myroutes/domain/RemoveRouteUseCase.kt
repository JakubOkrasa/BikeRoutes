package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository

class RemoveRouteUseCase(private val repository: RemoteRepository): UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
        repository.deleteRoute(params)
    }
}