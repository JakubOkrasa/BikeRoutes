package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository

class AddSegmentUseCase(private val repository: RemoteRepository): UseCase<Unit, Segment>() {
    override suspend fun action(params: Segment) {
        repository.addSegment(params)
    }
}
