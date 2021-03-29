package pl.jakubokrasa.bikeroutes.features.routerecording.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

interface RouteRepository {
    suspend fun getCurrentRoute(): Route
    suspend fun getCurrentRouteId(): Long


    fun getMyRoutes(): LiveData<List<Route>>

    suspend fun insertRoute(route: Route)

    suspend fun updateRouteName(routeId: Long, name: String)
    suspend fun updateRouteDescription(routeId: Long, description: String)
    suspend fun updateRouteDistance(routeId: Long, distance: Int)

    suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint)

    suspend fun deleteRoute(route: Route)

    suspend fun markRouteAsNotCurrent()

}