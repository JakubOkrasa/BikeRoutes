package pl.jakubokrasa.bikeroutes.features.segments.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.features.segments.domain.SegmentRepository
import pl.jakubokrasa.bikeroutes.features.segments.data.model.SegmentResponse
import pl.jakubokrasa.bikeroutes.features.segments.domain.model.Segment

class SegmentRepositoryImpl(
    private val firestore: FirebaseFirestore,
): SegmentRepository {
    override suspend fun getSegments(routeId: String): List<Segment> {
        return firestore.collection("segments")
            .whereEqualTo("routeId", routeId)
            .get()
            .await()
            .map { doc -> doc.toObject(SegmentResponse::class.java).toSegment() }
    }

    override suspend fun addSegment(segment: Segment) {
        val segmentDoc = firestore.collection("segments").document()

        firestore.runBatch { batch ->
            batch.set(segmentDoc, SegmentResponse(segment))
            batch.update(segmentDoc, "segmentId", segmentDoc.id)
        }.await()
    }

    override suspend fun removeSegment(segmentId: String) {
        firestore.collection("segments")
            .document(segmentId)
            .delete()
            .await()
    }
}