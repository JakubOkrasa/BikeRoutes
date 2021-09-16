package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.model.Route

class GetMyRoutesUseCase(
    private val repository: RouteRepository,
    private val auth: UserAuth
): UseCase<List<Route>, Unit>() {
    override suspend fun action(params: Unit): List<Route> {
        return repository.getMyRoutes(auth.getCurrentUserId())
    }
}
