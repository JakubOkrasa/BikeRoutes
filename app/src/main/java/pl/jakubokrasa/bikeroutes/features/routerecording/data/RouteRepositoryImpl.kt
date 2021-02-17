package pl.jakubokrasa.bikeroutes.features.routerecording.data

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RouteDao
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

class RouteRepositoryImpl(private val dao: RouteDao): RouteRepository {
    override suspend fun getCurrentRoute(): Route {
       return dao.getCurrentRoute().toRoute()
    }

    override suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint) {
        return dao.insertCurrentRoutePoint(geoPoint)
    }

}