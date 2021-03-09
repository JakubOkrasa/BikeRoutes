package pl.jakubokrasa.bikeroutes.features.routerecording.data

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RouteAndPointDao
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

class RouteRepositoryImpl(private val andPointDao: RouteAndPointDao): RouteRepository {
    override suspend fun getCurrentRoute(): Route {
       return andPointDao.getCurrentRoute().toRoute()
    }

//    override suspend fun insertRoute(routeCached: RouteCached) { // TODO: 2/17/2021 czy tu nie powinno być Route z w. danych? RouteRepository (interface) jest w w. domeny
//        andPointDao.insertRoute(routeCached)
//    }

    override suspend fun insertRoute(route: Route) {
        andPointDao.insertRoute(RouteCached(0, route.current)) //routeId=0 to be auto-generated
    }

    override suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint) {
        return andPointDao.insertCurrentRoutePoint(geoPoint)
    }

    override suspend fun markRouteAsNotCurrent() {
        andPointDao.markRouteAsNotCurrent()
    }

}