package pl.jakubokrasa.bikeroutes.features.routerecording.ui

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

interface RouteRepository {
    suspend fun getCurrentRoute(): Route

    suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint)

}