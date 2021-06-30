package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository

class GetSegmentsUseCase(remoteRepository: RemoteRepository): UseCase<List<Segment>, String> {
    override suspend fun action(params: String): List<Segment> {
        TODO("Not yet implemented")
    }
}