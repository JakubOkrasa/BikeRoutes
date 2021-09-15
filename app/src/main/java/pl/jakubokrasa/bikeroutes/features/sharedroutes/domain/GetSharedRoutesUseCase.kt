package pl.jakubokrasa.bikeroutes.features.sharedroutes.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.model.Route

class GetSharedRoutesUseCase(
    private val repository: RouteRepository,
    private val auth: UserAuth
) {
    suspend fun action() =
        repository.getSharedRoutes(auth.getCurrentUserId())

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
