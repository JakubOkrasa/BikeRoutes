package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository

class RemoveSegmentUseCase(private val repository: RemoteRepository): UseCase<Unit, String>() {
    override suspend fun action(params: String) {
        repository.removeSegment(params)
    }
}