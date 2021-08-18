package pl.jakubokrasa.bikeroutes.features.common.domain.repository

import pl.jakubokrasa.bikeroutes.features.common.domain.model.FilterData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

interface RouteRepository {
    suspend fun addRoute(route: Route, points: List<Point>)

    suspend fun getMyRoutes(uid: String): List<Route>
    suspend fun getMyRoutesWithFilter(uid: String, filterData: FilterData): List<Route>

    suspend fun updateRoute(route: Route)

    suspend fun deleteRoute(route: Route)

    suspend fun getSharedRoutes(uid: String): List<Route>
    suspend fun getSharedRoutesWithFilter(uid: String, filterData: FilterData): List<Route>
}