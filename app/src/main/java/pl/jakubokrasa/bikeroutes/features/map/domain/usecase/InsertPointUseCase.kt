package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData

class InsertPointUseCase(private val localRepository: LocalRepository): UseCase<Unit, GeoPointData>() {
    override suspend fun action(params: GeoPointData) = localRepository.insertPoint(params)
}