package pl.jakubokrasa.bikeroutes.features.routerecording.data

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RouteDao
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

class RouteRepositoryImpl(private val dao: RouteDao): RouteRepository {
    override suspend fun getCurrentRoute(): Route {
       return Route(1L, true, listOf(Point(1L, GeoPoint(10.32, 5.84))))
//       return dao.getCurrentRoute().toRoute()
    }

}