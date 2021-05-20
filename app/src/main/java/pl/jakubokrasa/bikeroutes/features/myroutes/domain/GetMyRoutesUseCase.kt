package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuth
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class GetMyRoutesUseCase(
    private val remoteRepository: RemoteRepository,
    private val auth: UserAuth
) {
    suspend fun action(dataFilterParams: DataFilterParams) =
        remoteRepository.getMyRoutes(auth.getCurrentUserId(), dataFilterParams.minDistance, dataFilterParams.maxDistance)

    operator fun invoke(
        dataFilterParams: DataFilterParams,
        scope: CoroutineScope,
        onResult: (Result<List<Route>>) -> Unit = {}
        ) {
            scope.launch {
                val result = withContext(Dispatchers.IO) {
                    runCatching { return@runCatching action(dataFilterParams) }
                }
                onResult(result)
            }
        }
}

data class DataFilterParams(
    val minDistance: Int = -1,
    val maxDistance: Int = -1
)
