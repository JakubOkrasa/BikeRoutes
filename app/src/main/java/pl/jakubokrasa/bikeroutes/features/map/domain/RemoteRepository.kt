package pl.jakubokrasa.bikeroutes.features.map.domain

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

interface RemoteRepository {

    //MY ROUTES
    suspend fun addRoute(route: Route, points: List<Point>)

    suspend fun getMyRoutes(uid: String): List<Route>
    suspend fun getMyRoutesWithFilter(uid: String, filterData: FilterData): List<Route>

    suspend fun updateRoute(route: Route)

    suspend fun deleteRoute(route: Route)

    suspend fun getPoints(routeId: String): List<Point>

    suspend fun addPhoto(routeId: String, localPath: String, sharingType: SharingType)


    //SHARED ROUTES
    suspend fun getSharedRoutes(uid: String): List<Route>
    suspend fun getSharedRoutesWithFilter(uid: String, filterData: FilterData): List<Route>
}