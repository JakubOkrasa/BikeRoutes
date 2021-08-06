package pl.jakubokrasa.bikeroutes.features.reviews.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.reviews.domain.model.Review

class AddReviewUseCase(private val repository: RemoteRepository): UseCase<Unit, Review>() {
    override suspend fun action(params: Review) {
        repository.addReview(params)
    }
}