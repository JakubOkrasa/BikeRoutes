package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.core.user.auth.UserAuth
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class GetMyRoutesWithFilterUseCase(
    private val remoteRepository: RemoteRepository,
    private val auth: UserAuth
) {
    suspend fun action(filterData: FilterData) =
        remoteRepository.getMyRoutesWithFilter(auth.getCurrentUserId(), filterData)

    operator fun invoke(
        filterData: FilterData,
        scope: CoroutineScope,
        onResult: (Result<List<Route>>) -> Unit = {}
        ) {
            scope.launch {
                val result = withContext(Dispatchers.IO) {
                    runCatching { return@runCatching action(filterData) }
                }
                onResult(result)
            }
        }
}

data class FilterData(
    val minDistanceKm: Int? = null,
    val maxDistanceKm: Int? = null
)
