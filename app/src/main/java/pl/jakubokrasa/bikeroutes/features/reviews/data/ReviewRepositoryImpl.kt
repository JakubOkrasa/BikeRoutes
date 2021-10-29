package pl.jakubokrasa.bikeroutes.features.reviews.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.features.reviews.data.model.ReviewResponse
import pl.jakubokrasa.bikeroutes.features.reviews.domain.ReviewRepository
import pl.jakubokrasa.bikeroutes.features.reviews.domain.model.Review

class ReviewRepositoryImpl(
    private val firestore: FirebaseFirestore,
): ReviewRepository {

    override suspend fun getReviews(routeId: String): List<Review> {
        return firestore.collection("reviews")
            .whereEqualTo("routeId", routeId)
            .get()
            .await()
            .map { doc -> doc.toObject(ReviewResponse::class.java).toReview() }
    }

    override suspend fun addReview(review: Review) {
        val reviewDoc = firestore.collection("reviews").document()

        firestore.runBatch { batch ->
            batch.set(reviewDoc, ReviewResponse(review))
            batch.update(reviewDoc, "reviewId", reviewDoc.id)
        }.await()
    }

    override suspend fun updateReview(review: Review) {
        firestore.collection("reviews")
            .document(review.reviewId)
            .set(ReviewResponse(review))
            .await()
    }

    override suspend fun removeReview(reviewId: String) {
        firestore.collection("reviews")
            .document(reviewId)
            .delete()
            .await()
    }

}