package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import androidx.lifecycle.LiveData
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCaseLiveData
import pl.jakubokrasa.bikeroutes.features.map.domain.PointLocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class GetPointsUseCase(private val repository: PointLocalRepository): UseCaseLiveData<LiveData<List<Point>>, Unit>() {
    override fun action(params: Unit): LiveData<List<Point>> {
        return repository.getPoints()
    }
}