package pl.jakubokrasa.bikeroutes.features.points.domain

import androidx.lifecycle.LiveData
import pl.jakubokrasa.bikeroutes.core.base.domain.SynchronousUseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class GetPointsFromLocalSyncUseCase(private val repository: PointLocalRepository): SynchronousUseCase<LiveData<List<Point>>, Unit>() {
    override fun action(params: Unit): LiveData<List<Point>> {
        return repository.getPointsLiveData()
    }
}