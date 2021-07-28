package pl.jakubokrasa.bikeroutes.features.reviews.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository

class RemoveReviewUseCase(private val repository: RemoteRepository): UseCase<Unit, String>() {
    override suspend fun action(params: String) {
        repository.removeReview(params)
    }
}