package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.core.util.doesRouteCoversMap
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.FilterData
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment

class GetMyRoutesWithFilterUseCase(
    private val repository: RouteRepository,
    private val auth: UserAuth
): UseCase<List<Route>, FilterData>() {
    override suspend fun action(params: FilterData): List<Route> {
        var minDistanceMeters: Int? = null
        var maxDistanceMeters: Int? = null
        var mapBB: BoundingBoxData? = null

        params.minDistanceKm?.let {
            minDistanceMeters =
                if(it == 0) null
                else it * 1000
        }
        params.maxDistanceKm?.let {
            maxDistanceMeters =
                if (it == MyRoutesFragment.DISTANCE_SLIDER_VALUE_TO.toInt()) null
                else it * 1000
        }
        params.boundingBoxData?.let {
            mapBB = if (isBoundingBoxNotInitialized(it)) null
            else it
        }

        val routes = repository.getMyRoutes(auth.getCurrentUserId()).toMutableList()

        minDistanceMeters?.let { minDistance ->
            val lessThanMinDistanceRoutes: List<Route> = routes.filter { minDistance > it.distance }
            routes -= lessThanMinDistanceRoutes
        }

        maxDistanceMeters?.let { maxDistance ->
            val moreThanMaxDistanceRoutes: List<Route> = routes.filter { maxDistance < it.distance }
            routes -= moreThanMaxDistanceRoutes
        }

        mapBB?.let { mapBox ->
            return routes.filter { route ->
                doesRouteCoversMap(route.boundingBoxData, mapBox)
            }
        }
        return routes
    }

    private fun isBoundingBoxNotInitialized(it: BoundingBoxData) =
        it.latNorth == 0.0 && it.latSouth == 0.0 && it.lonEast == 0.0 && it.lonWest == 0.0
}
