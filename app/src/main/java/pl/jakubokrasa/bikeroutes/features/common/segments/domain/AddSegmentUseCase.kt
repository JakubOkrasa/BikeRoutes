package pl.jakubokrasa.bikeroutes.features.common.segments.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.SegmentRepository
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment

class AddSegmentUseCase(private val repository: SegmentRepository): UseCase<Unit, Segment>() {
    override suspend fun action(params: Segment) {
        repository.addSegment(params)
    }
}
