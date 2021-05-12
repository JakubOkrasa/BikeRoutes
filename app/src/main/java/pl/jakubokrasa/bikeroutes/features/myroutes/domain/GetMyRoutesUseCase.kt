package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCaseLiveData
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuth
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.RouteRepository

class GetMyRoutesUseCase(
    private val routeRepository: RouteRepository,
    private val auth: UserAuth
) {
    suspend fun action() =
        routeRepository.getMyRoutes(auth.getCurrentUserId())

    operator fun invoke(
        scope: CoroutineScope,
        onResult: (Result<List<RouteResponse>>) -> Unit = {}
        ) {
            scope.launch {
                val result = withContext(Dispatchers.IO) {
                    runCatching { return@runCatching action() }
                }
                onResult(result)
            }
        }
}
