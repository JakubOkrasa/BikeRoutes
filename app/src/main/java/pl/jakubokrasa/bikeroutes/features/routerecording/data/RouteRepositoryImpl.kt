package pl.jakubokrasa.bikeroutes.features.routerecording.data

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RouteAndPointDao
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

class RouteRepositoryImpl(private val routeAndPointDao: RouteAndPointDao): RouteRepository {
    override suspend fun getCurrentRoute(): Route {
       return routeAndPointDao.getCurrentRoute().toRoute()
    }
    override suspend fun getCurrentRouteId(): Long {
        return routeAndPointDao.getCurrentRouteId()
    }

    override suspend fun getMyRoutes(): List<Route> {
        return routeAndPointDao.getMyRoutes().map { it.toRoute() }
    }

    override suspend fun insertRoute(route: Route) {
        routeAndPointDao.insertRoute(RouteCached(0, route.name, route.description, route.current, route.distance,  route.sharingType)) //routeId=0 to be auto-generated
    }

    override suspend fun updateRouteName(routeId: Long, name: String) {
        routeAndPointDao.updateRouteName(routeId, name)
    }

    override suspend fun updateRouteDescription(routeId: Long, description: String) {
        routeAndPointDao.updateRouteDescription(routeId, description)
    }

    override suspend fun updateRouteDistance(routeId: Long, distance: Int) {
        routeAndPointDao.updateRouteDistance(routeId, distance)
    }

    override suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint) {
        return routeAndPointDao.insertCurrentRoutePoint(geoPoint)
    }

    override suspend fun markRouteAsNotCurrent() {
        routeAndPointDao.markRouteAsNotCurrent()
    }

}