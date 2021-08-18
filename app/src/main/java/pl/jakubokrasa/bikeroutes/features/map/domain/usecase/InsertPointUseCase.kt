package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.PointLocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData

class InsertPointUseCase(private val repository: PointLocalRepository): UseCase<Unit, GeoPointData>() {
    override suspend fun action(params: GeoPointData) = repository.insertPoint(params)
}