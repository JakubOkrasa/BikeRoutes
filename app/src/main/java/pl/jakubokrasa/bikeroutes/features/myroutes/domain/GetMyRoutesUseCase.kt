package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class GetMyRoutesUseCase(
    private val repository: RouteRepository,
    private val auth: UserAuth
) {
    suspend fun action() =
        repository.getMyRoutes(auth.getCurrentUserId())

    operator fun invoke(
        scope: CoroutineScope,
        onResult: (Result<List<Route>>) -> Unit = {}
        ) {
            scope.launch {
                val result = withContext(Dispatchers.IO) {
                    runCatching { return@runCatching action() }
                }
                onResult(result)
            }
        }
}
