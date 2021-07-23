package pl.jakubokrasa.bikeroutes.features.reviews.domain.model

data class Review(
    val userId: String,
    val routeId: String,
    val createdAt: Long,
    val content: String,
)
