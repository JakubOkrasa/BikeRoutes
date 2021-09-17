package pl.jakubokrasa.bikeroutes.features.common.points.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase

class DeletePointsUseCase(private val repository: PointLocalRepository): UseCase<Unit, Unit>() {
    override suspend fun action(params: Unit) {
        repository.deletePoints()
    }
}