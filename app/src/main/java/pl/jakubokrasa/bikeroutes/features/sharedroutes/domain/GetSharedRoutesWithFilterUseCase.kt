package pl.jakubokrasa.bikeroutes.features.sharedroutes.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.features.common.domain.doesRouteCoversMap
import pl.jakubokrasa.bikeroutes.features.common.domain.model.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.common.domain.model.FilterData
import pl.jakubokrasa.bikeroutes.features.common.domain.repository.RouteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class GetSharedRoutesWithFilterUseCase(
    private val repository: RouteRepository,
    private val auth: UserAuth
) {
    suspend fun action(filterData: FilterData): List<Route>  {
        var mapBB: BoundingBoxData? = null
        filterData.boundingBoxData?.let {
            mapBB = if(isBoundingBoxNotInitialized(it)) null
            else it
        }
        val filteredList = repository.getSharedRoutesWithFilter(auth.getCurrentUserId(), filterData)
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


