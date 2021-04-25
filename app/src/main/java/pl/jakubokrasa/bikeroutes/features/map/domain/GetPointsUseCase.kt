package pl.jakubokrasa.bikeroutes.features.map.domain

import androidx.lifecycle.LiveData
import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteRepository

class GetPointsUseCase(private val routeRepository: RouteRepository): UseCase<LiveData<List<PointCached>>, Unit>() {
    override suspend fun action(params: Unit): LiveData<List<PointCached>> {
        return routeRepository.getPoints()
    }
}