package pl.jakubokrasa.bikeroutes.features.map.data.remote

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.myroutes.data.model.PointDocument
import java.lang.RuntimeException

class RouteRepositoryImpl(
    private val firestore: FirebaseFirestore): RouteRepository {
    override suspend fun addRoute(route: Route) {
        val routeResponse = RouteResponse(route.userId, route.name, route.description, route.sharingType, route.distance)
        val routeDoc = firestore.collection("routes").document()
        val pointsMap = mapOf("pointsArray" to route.points.map {PointResponse(it)})
        val pointsDoc = firestore.collection("points").document(routeDoc.id)
        val userDoc = firestore.collection("users").document(route.userId)

        //WRITE BATCHES WORK OFFLINE
        firestore.runBatch { batch ->
            //save RouteResponse
            batch.set(routeDoc, routeResponse)

            //save Points
            batch.set(pointsDoc, pointsMap) // nie wiem czy mogę wziąć id z poprzedniej linii ( .id ), skoro to będzie się wykonywać współbieżnie

            //assign route to a user
            batch.update(userDoc, "routes", FieldValue.arrayUnion(routeDoc.id))
        }.await()

    }

    override suspend fun getMyRoutes(uid: String): List<RouteResponse> {
        val routeResponseList = ArrayList<RouteResponse>()
        val documents = firestore.collection("routes").whereEqualTo("userId", uid).get().await().documents
        for (doc in documents) doc.toObject(RouteResponse::class.java)?.let { routeResponseList.add(it) }
        return routeResponseList
    }

    override suspend fun getPoints(routeId: String): List<PointResponse> {
//        val pointResponseList = ArrayList<PointResponse>()
//        val doc = firestore.document("points/$routeId")
//        val doc = firestore.collection("points").document(routeId).get("pointsArray").await()
        val doc = firestore.collection("points").document(routeId).get().await()
        return doc.toObject(PointDocument::class.java)?.pointsArray ?: throw RuntimeException("no points in the route")
//        doc.toObject(ArrayList<Poi>)
    }

    companion object {
        val LOG = RouteRepositoryImpl::class.simpleName
    }
}