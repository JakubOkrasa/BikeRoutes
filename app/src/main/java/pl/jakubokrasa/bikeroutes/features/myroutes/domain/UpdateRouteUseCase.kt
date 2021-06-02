package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class UpdateRouteUseCase(private val remoteRepository: RemoteRepository): UseCase<Unit, Route>() {
    override suspend fun action(params: Route) {
        remoteRepository.updateRoute(params)
    }

}