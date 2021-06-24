package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.features.common.domain.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.common.domain.doesRouteCoversMap
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class GetMyRoutesWithFilterUseCase(
    private val remoteRepository: RemoteRepository,
    private val auth: UserAuth
) {
    suspend fun action(filterData: FilterData): List<Route> {
        var mapBB: BoundingBoxData? = null
        filterData.boundingBoxData?.let {
            mapBB = if(isBoundingBoxNotInitialized(it)) null
            else it
        }
        val filteredList = remoteRepository.getMyRoutesWithFilter(auth.getCurrentUserId(), filterData)
        mapBB?.let {
            return filteredList.filter { route ->
                doesRouteCoversMap(route.boundingBoxData, mapBB!!)
            }
        }
        return filteredList
    }

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


    private fun isBoundingBoxNotInitialized(it: BoundingBoxData) =
        it.latNorth == 0.0 && it.latSouth == 0.0 && it.lonEast == 0.0 && it.lonWest == 0.0
}
