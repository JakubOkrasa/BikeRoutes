package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.PointLocalRepository

class DeletePointsUseCase(private val repository: PointLocalRepository): UseCase<Unit, Unit>() {
    override suspend fun action(params: Unit) {
        repository.deletePoints()
    }
}