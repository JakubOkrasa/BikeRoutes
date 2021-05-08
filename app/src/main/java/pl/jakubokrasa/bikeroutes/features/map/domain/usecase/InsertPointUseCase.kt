package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository

class InsertPointUseCase(private val pointRepository: PointRepository): UseCase<Unit, GeoPoint>() {
    override suspend fun action(params: GeoPoint) = pointRepository.insertPoint(params)
}