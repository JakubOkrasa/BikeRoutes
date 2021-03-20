package pl.jakubokrasa.bikeroutes.features.routerecording.data

import android.util.Log
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RouteAndPointDao
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

class RouteRepositoryImpl(private val routeAndPointDao: RouteAndPointDao): RouteRepository {
    override suspend fun getCurrentRoute(): Route {
       return routeAndPointDao.getCurrentRoute().toRoute()
    }

    override suspend fun getMyRoutes(): List<Route> {
        Log.d("RouteRepositoryImpl", "getMyRoutes() call")

        return routeAndPointDao.getMyRoutes().map { it.toRoute() }
            .also { it.forEach{
                Log.d("RouteRepositoryImpl", String.format("got route with %d point(s).", it.points.size))
            }}
    }

//    override suspend fun insertRoute(routeCached: RouteCached) { // TODO: 2/17/2021 czy tu nie powinno być Route z w. danych? RouteRepository (interface) jest w w. domeny
//        andPointDao.insertRoute(routeCached)
//    }

    override suspend fun insertRoute(route: Route) {
        routeAndPointDao.insertRoute(RouteCached(0, route.name, route.description, route.current, route.distance,  route.sharingType)) //routeId=0 to be auto-generated
    }

    override suspend fun updateCurrentRouteName(name: String) {
        routeAndPointDao.updateCurrentRouteName(name)
    }

    override suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint) {
        return routeAndPointDao.insertCurrentRoutePoint(geoPoint)
    }

    override suspend fun markRouteAsNotCurrent() {
        routeAndPointDao.markRouteAsNotCurrent()
    }

}