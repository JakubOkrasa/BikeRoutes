package pl.jakubokrasa.bikeroutes.features.routerecording.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RouteAndPointDao
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteRepository

class RouteRepositoryImpl(private val routeAndPointDao: RouteAndPointDao): RouteRepository {
    override suspend fun getCurrentRoute(): Route {
       return routeAndPointDao.getCurrentRoute().toRoute()
    }
    override suspend fun getCurrentRouteId(): Long {
        return routeAndPointDao.getCurrentRouteId()
    }

    override fun getMyRoutes(): LiveData<List<Route>> {
        Log.i("i", "id")
        return routeAndPointDao.getMyRoutes().map { list ->
            list.map { it.toRoute() }
        }
        //.map { it.value.toRoute() }
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

    override suspend fun deleteRoute(route: Route) {
        routeAndPointDao.deleteRoute(RouteCached(route))
    }

    override suspend fun markRouteAsNotCurrent() {
        routeAndPointDao.markRouteAsNotCurrent()
    }

}