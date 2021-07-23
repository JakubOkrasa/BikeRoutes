package pl.jakubokrasa.bikeroutes.features.reviews.presentation.model

import pl.jakubokrasa.bikeroutes.features.reviews.domain.model.Review

data class ReviewDisplayable(
    val userId: String,
    val routeId: String,
    val createdAt: Long,
    val content: String,
)
{
    constructor(review: Review): this(
        userId = review.userId,
        routeId = review.routeId,
        createdAt = review.createdAt,
        content = review.content
    )

    fun toReview(): Review {
        return Review(
            userId = this.userId,
            routeId = this.routeId,
            createdAt = this.createdAt,
            content = this.content
        )
    }
}