package pl.jakubokrasa.bikeroutes.features.map.domain

import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.FilterData

interface RemoteRepository {
    suspend fun addRoute(route: Route, points: List<Point>)

    suspend fun getMyRoutes(uid: String): List<Route>
    suspend fun getMyRoutesWithFilter(uid: String, filterData: FilterData): List<Route>

    suspend fun getPoints(routeId: String): List<Point>
}