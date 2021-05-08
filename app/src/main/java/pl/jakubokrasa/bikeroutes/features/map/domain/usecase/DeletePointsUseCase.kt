package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository

class DeletePointsUseCase(private val pointRepository: PointRepository): UseCase<Unit, Unit>() {
    override suspend fun action(params: Unit) {
        pointRepository.deletePoints()
    }
}