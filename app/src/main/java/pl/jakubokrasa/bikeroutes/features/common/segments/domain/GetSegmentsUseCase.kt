package pl.jakubokrasa.bikeroutes.features.common.segments.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment

class GetSegmentsUseCase(private val repository: SegmentRepository): UseCase<List<Segment>, String>() {
    override suspend fun action(params: String): List<Segment> {
        return repository.getSegments(params)
    }
}