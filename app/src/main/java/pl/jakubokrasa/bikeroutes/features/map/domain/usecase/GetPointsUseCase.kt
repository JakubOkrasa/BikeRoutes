package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import pl.jakubokrasa.bikeroutes.core.base.UseCaseLiveData
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class GetPointsUseCase(private val pointRepository: PointRepository): UseCaseLiveData<LiveData<List<Point>>, Unit>() {
    override fun action(params: Unit): LiveData<List<Point>> {
        return pointRepository.getPoints().map { list -> list.map {
            it.toPoint()
        }}
    }
}