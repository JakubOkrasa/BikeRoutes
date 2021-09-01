package pl.jakubokrasa.bikeroutes.features.common.reviews.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.model.Review

class UpdateReviewUseCase(private val repository: ReviewRepository): UseCase<Unit, Review>() {
    override suspend fun action(params: Review) {
        repository.updateReview(params)
    }
}