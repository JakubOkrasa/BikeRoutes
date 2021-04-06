package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteRepository
import java.sql.Timestamp
import java.util.*

class InsertCurrentPointUseCase(private val routeRepository: RouteRepository): UseCase<Unit, GeoPoint>() {
    override suspend fun action(params: GeoPoint) = routeRepository.insertCurrentRoutePoint(params)
}