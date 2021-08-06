package pl.jakubokrasa.bikeroutes.features.reviews.presentation.model

import pl.jakubokrasa.bikeroutes.features.reviews.domain.model.Review

data class ReviewDisplayable(
    val reviewId: String,
    val userId : String,
    val createdBy : String,
    val routeId: String,
    var createdAt: Long,
    var content: String,
)
{
    constructor(review: Review): this(
        reviewId = review.reviewId,
        userId = review.userId,
        createdBy = review.createdBy,
        routeId = review.routeId,
        createdAt = review.createdAt,
        content = review.content
    )

    fun toReview(): Review {
        return Review(
            reviewId = this.reviewId,
            userId = this.userId,
            createdBy = this.createdBy,
            routeId = this.routeId,
            createdAt = this.createdAt,
            content = this.content
        )
    }
}