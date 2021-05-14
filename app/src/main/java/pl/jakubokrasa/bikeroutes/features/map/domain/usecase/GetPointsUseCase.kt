package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCaseLiveData
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class GetPointsUseCase(private val localRepository: LocalRepository): UseCaseLiveData<LiveData<List<Point>>, Unit>() {
    override fun action(params: Unit): LiveData<List<Point>> {
        return localRepository.getPoints()
    }
}