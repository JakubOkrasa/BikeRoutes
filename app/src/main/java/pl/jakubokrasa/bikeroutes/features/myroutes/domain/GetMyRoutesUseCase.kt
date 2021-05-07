package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository

class GetMyRoutesUseCase(private val pointRepository: PointRepository):
    UseCase<List<Route>, Unit>() {

    override suspend fun action(params: Unit) = TODO()  // pointRepository.getMyRoutes()
}