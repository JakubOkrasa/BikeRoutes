package pl.jakubokrasa.bikeroutes.features.routes.domain

import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routes.domain.model.Route

interface RouteRepository {
    suspend fun addRoute(route: Route, points: List<Point>)

    suspend fun getMyRoutes(uid: String): List<Route>

    suspend fun updateRoute(route: Route)

    suspend fun deleteRoute(route: Route)

    suspend fun getSharedRoutes(uid: String): List<Route>
}