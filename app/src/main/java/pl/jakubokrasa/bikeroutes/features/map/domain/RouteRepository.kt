package pl.jakubokrasa.bikeroutes.features.map.domain

import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

interface RouteRepository {
    suspend fun addRoute(route: Route)

    suspend fun getMyRoutes(uid: String): List<RouteResponse>

    suspend fun getPoints(routeId: String): List<PointResponse>
}