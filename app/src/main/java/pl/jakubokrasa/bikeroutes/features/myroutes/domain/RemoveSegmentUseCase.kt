package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.domain.repository.SegmentRepository

class RemoveSegmentUseCase(private val repository: SegmentRepository): UseCase<Unit, String>() {
    override suspend fun action(params: String) {
        repository.removeSegment(params)
    }
}