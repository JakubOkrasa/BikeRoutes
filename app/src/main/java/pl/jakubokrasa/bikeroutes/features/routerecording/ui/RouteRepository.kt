package pl.jakubokrasa.bikeroutes.features.routerecording.ui

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

interface RouteRepository {
    suspend fun getCurrentRoute(): Route

//    suspend fun insertRoute(routeCached: RouteCached)

    suspend fun insertRoute(route: Route)

    suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint)

}