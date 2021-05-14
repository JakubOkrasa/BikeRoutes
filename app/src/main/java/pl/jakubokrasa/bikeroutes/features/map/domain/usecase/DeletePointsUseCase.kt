package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository

class DeletePointsUseCase(private val localRepository: LocalRepository): UseCase<Unit, Unit>() {
    override suspend fun action(params: Unit) {
        localRepository.deletePoints()
    }
}