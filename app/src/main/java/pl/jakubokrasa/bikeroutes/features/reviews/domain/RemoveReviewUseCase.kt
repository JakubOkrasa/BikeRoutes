package pl.jakubokrasa.bikeroutes.features.reviews.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase

class RemoveReviewUseCase(private val repository: ReviewRepository): UseCase<Unit, String>() {
    override suspend fun action(params: String) {
        repository.removeReview(params)
    }
}