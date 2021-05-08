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

class RouteRepositoryImpl(private val firestore: FirebaseFirestore): RouteRepository {
    override suspend fun addRoute(route: Route) {

//        //save RouteResponse
//        val routeResponse = RouteResponse(route.userId, route.name, route.description, route.sharingType, route.distance)
//        val routeId = firestore.collection("routes").add(routeResponse).await().id
//
//        //save List<PointResponse>
//        val pointsMap = mapOf("pointsArray" to route.points.map {PointResponse(it)})
//        firestore.collection("points").document(routeId).set(pointsMap).await()
//
//        //assign route to a user
//        val userRef = firestore.collection("users").document(route.userId)
//        userRef.update("routes", FieldValue.arrayUnion(routeId)).await()


        // TRANSACTIONS DO NOT WORK OFFLINE | all inside worked
//        firestore.runTransaction {
//            transaction ->
//
//            //save RouteResponse
//            val routeResponse = RouteResponse(route.userId, route.name, route.description, route.sharingType, route.distance)
//            val routesRef = firestore.collection("routes")
//            val routeDoc = routesRef.document()
//            transaction.set(routeDoc, routeResponse)
//
//            //save Points
//            val pointsMap = mapOf("pointsArray" to route.points.map {PointResponse(it)})
//            val pointsDoc = firestore.collection("points").document(routeDoc.id)
//            transaction.set(pointsDoc, pointsMap) // nie wiem czy mogę wziąć id z poprzedniej linii ( .id ), skoro to będzie się wykonywać współbieżnie
//
//            //assign route to a user
//            val userDoc = firestore.collection("users").document(route.userId)
//            transaction.update(userDoc, "routes", FieldValue.arrayUnion(routeDoc.id))
//
//        }.await()

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

    companion object {
        val LOG = RouteRepositoryImpl::class.simpleName
    }
}