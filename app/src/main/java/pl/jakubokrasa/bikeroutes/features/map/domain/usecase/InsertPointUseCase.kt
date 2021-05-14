package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository

class InsertPointUseCase(private val localRepository: LocalRepository): UseCase<Unit, GeoPoint>() {
    override suspend fun action(params: GeoPoint) = localRepository.insertPoint(params)
}