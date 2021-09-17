package pl.jakubokrasa.bikeroutes.features.sharedroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.model.Route

class GetSharedRoutesUseCase(
    private val repository: RouteRepository,
    private val auth: UserAuth
): UseCase<List<Route>, Unit>() {
    override suspend fun action(params: Unit) =
        repository.getSharedRoutes(auth.getCurrentUserId())
}
