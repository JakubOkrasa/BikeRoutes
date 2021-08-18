package pl.jakubokrasa.bikeroutes.features.common.domain.repository

import pl.jakubokrasa.bikeroutes.features.reviews.domain.model.Review

interface ReviewRepository {
    suspend fun getReviews(routeId: String): List<Review>
    suspend fun addReview(review: Review)
    suspend fun updateReview(review: Review)
    suspend fun removeReview(reviewId: String)
}