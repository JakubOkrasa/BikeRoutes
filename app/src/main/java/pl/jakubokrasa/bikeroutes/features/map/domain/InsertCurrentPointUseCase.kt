package pl.jakubokrasa.bikeroutes.features.map.domain

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteRepository

class InsertCurrentPointUseCase(private val routeRepository: RouteRepository): UseCase<Unit, GeoPoint>() {
    override suspend fun action(params: GeoPoint) = routeRepository.insertCurrentRoutePoint(params)
}