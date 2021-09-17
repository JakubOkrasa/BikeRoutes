package pl.jakubokrasa.bikeroutes.features.common.segments.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase

class RemoveSegmentUseCase(private val repository: SegmentRepository): UseCase<Unit, String>() {
    override suspend fun action(params: String) {
        repository.removeSegment(params)
    }
}