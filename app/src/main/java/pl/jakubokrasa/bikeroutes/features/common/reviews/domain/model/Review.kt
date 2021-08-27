package pl.jakubokrasa.bikeroutes.features.common.reviews.domain.model

data class Review(
    val reviewId: String,
    val userId: String,
    val createdBy: String,
    val routeId: String,
    val createdAt: Long,
    val content: String,
)
