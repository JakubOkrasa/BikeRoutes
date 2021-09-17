package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class GetPointsFromRemoteUseCase(private val repository: PointRemoteRepository): UseCase<List<Point>, String>() {
    override suspend fun action(params: String): List<Point> {
        return repository.getPoints(params)
    }
}