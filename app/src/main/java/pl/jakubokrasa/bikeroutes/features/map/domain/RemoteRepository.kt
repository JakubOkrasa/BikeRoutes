package pl.jakubokrasa.bikeroutes.features.map.domain

import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

interface RemoteRepository {
    suspend fun addRoute(route: Route, points: List<Point>)

    suspend fun getMyRoutes(uid: String): List<Route>

    suspend fun getPoints(routeId: String): List<Point>
}