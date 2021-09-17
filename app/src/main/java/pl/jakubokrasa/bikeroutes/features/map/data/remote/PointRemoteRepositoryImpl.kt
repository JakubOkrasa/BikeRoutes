package pl.jakubokrasa.bikeroutes.features.map.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.myroutes.data.model.PointDocument

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