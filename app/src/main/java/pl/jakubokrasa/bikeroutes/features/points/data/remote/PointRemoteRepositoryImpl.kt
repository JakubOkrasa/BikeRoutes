package pl.jakubokrasa.bikeroutes.features.points.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.points.data.remote.model.PointDocument
import pl.jakubokrasa.bikeroutes.features.points.domain.PointRemoteRepository

class PointRemoteRepositoryImpl(
    private val firestore: FirebaseFirestore,
): PointRemoteRepository {

    override suspend fun getPoints(routeId: String): List<Point> {
        val doc = firestore.collection("points").document(routeId).get().await()
        return doc
            .toObject(PointDocument::class.java)
            ?.pointsArray
            ?.map { it.toPoint() } ?: throw RuntimeException(
            "no points in the route")
    }
}