package pl.jakubokrasa.bikeroutes.features.map.presentation

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

interface RouteRepository {
    suspend fun getCurrentRoute(): Route
    suspend fun getCurrentRouteId(): Long


    suspend fun getMyRoutes(): List<Route>

    suspend fun insertRoute(route: Route)

    suspend fun updateRouteName(routeId: Long, name: String)
    suspend fun updateRouteDescription(routeId: Long, description: String)
    suspend fun updateRouteDistance(routeId: Long, distance: Int)

    suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint)

    suspend fun deleteRoute(route: Route)

    suspend fun markRouteAsNotCurrent()

}