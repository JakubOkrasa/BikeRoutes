package pl.jakubokrasa.bikeroutes.features.common.segments.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository

class GetSegmentsUseCase(private val repository: RemoteRepository): UseCase<List<Segment>, String>() {
    override suspend fun action(params: String): List<Segment> {
        return repository.getSegments(params)
    }
}