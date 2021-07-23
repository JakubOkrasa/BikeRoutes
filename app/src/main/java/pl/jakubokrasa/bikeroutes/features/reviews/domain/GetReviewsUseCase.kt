package pl.jakubokrasa.bikeroutes.features.reviews.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.reviews.domain.model.Review

class GetReviewsUseCase(private val repository: RemoteRepository): UseCase<List<Review>, String>() {
    override suspend fun action(params: String): List<Review> {
        repository.getReviews(params)
    }
}