package pl.jakubokrasa.bikeroutes.features.map.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.base.UseCaseLiveData
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteRepository

class GetPointsUseCase(private val routeRepository: RouteRepository): UseCaseLiveData<LiveData<List<Point>>, Unit>() {
    override fun action(params: Unit): LiveData<List<Point>> {
        return routeRepository.getPoints().map { list -> list.map {
            it.toPoint()
        }}
    }
}