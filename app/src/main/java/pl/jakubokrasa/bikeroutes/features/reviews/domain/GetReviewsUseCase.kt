package pl.jakubokrasa.bikeroutes.features.reviews.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.reviews.domain.model.Review

class GetReviewsUseCase(private val repository: ReviewRepository): UseCase<List<Review>, String>() {
    override suspend fun action(params: String): List<Review> {
        return repository.getReviews(params)
    }
}