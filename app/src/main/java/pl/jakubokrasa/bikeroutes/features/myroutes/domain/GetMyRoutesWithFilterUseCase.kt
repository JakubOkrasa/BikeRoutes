package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.features.common.domain.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class GetMyRoutesWithFilterUseCase(
    private val remoteRepository: RemoteRepository,
    private val auth: UserAuth
) {
    suspend fun action(filterData: FilterData): List<Route> {
        var mapBB: BoundingBoxData? = null
        filterData.boundingBoxData?.let {
            mapBB = if(it.latNorth == 0.0 && it.latSouth == 0.0 && it.lonEast == 0.0 && it.lonWest == 0.0) null
                else it
        }
        val introFilteredList = remoteRepository.getMyRoutesWithFilter(auth.getCurrentUserId(), filterData)
        mapBB?.let {
            val outroFilteredList = introFilteredList.filter { route ->
                doesRouteCoversMap(route.boundingBoxData, mapBB!!) //todo debug why it not returns true for route in Kraków
            }
            return outroFilteredList
        }
            val outroFilteredList = introFilteredList
            return outroFilteredList
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

    private fun doesRouteCoversMap(routeBB: BoundingBoxData, mapBB: BoundingBoxData):  Boolean  {
        val result = doesRouteCoversMapVertically(routeBB, mapBB) && doesRouteCoversMapHorizontally(routeBB, mapBB)
        Log.e("FILTER::", result.toString())
        return result
    }

    private fun doesRouteCoversMapHorizontally(
        routeBB: BoundingBoxData, mapBB: BoundingBoxData
    ): Boolean {
        val result = doesRouteCoversMapAndBottomIsOutside(routeBB, mapBB) || doesRouteCoversMapAndTopIsOutside(
            routeBB,
            mapBB) || isRouteBetweenVertically(routeBB, mapBB)
        Log.e("FILTER horizontal::", result.toString())
        return result
    }

    private fun doesRouteCoversMapVertically(
        routeBB: BoundingBoxData, mapBB: BoundingBoxData
    ) = doesRouteCoversMapAndLeftIsOutside(routeBB, mapBB) || doesRouteCoversMapAndRightIsOutside(
        routeBB, mapBB) || isRouteBetweenHorizontally(routeBB, mapBB)


    private fun doesRouteCoversMapAndBottomIsOutside(
        routeBB: BoundingBoxData, mapBB: BoundingBoxData
    ) = (routeBB.latSouth < mapBB.latSouth && routeBB.latNorth > mapBB.latSouth)

    private fun doesRouteCoversMapAndTopIsOutside(
        routeBB: BoundingBoxData, mapBB: BoundingBoxData
    ) = (routeBB.latNorth > mapBB.latNorth && routeBB.latSouth < mapBB.latNorth)

    private fun isRouteBetweenVertically(
        routeBB: BoundingBoxData, mapBB: BoundingBoxData
    ) = (routeBB.latNorth < mapBB.latNorth && routeBB.latSouth > mapBB.latSouth)


    private fun doesRouteCoversMapAndLeftIsOutside(
        routeBB: BoundingBoxData, mapBB: BoundingBoxData
    ) = (routeBB.lonWest < mapBB.lonWest && routeBB.lonEast > mapBB.lonWest)

    private fun doesRouteCoversMapAndRightIsOutside(
        routeBB: BoundingBoxData, mapBB: BoundingBoxData
    ) = (routeBB.lonEast > mapBB.lonEast && routeBB.lonWest < mapBB.lonEast)

    private fun isRouteBetweenHorizontally(
        routeBB: BoundingBoxData, mapBB: BoundingBoxData
    ) = (routeBB.lonEast < mapBB.lonEast && routeBB.lonWest > mapBB.lonWest)
}
