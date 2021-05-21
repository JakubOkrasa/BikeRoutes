package pl.jakubokrasa.bikeroutes.features.map.data.remote

import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.myroutes.data.model.PointDocument
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO

class RemoteRepositoryImpl(
    private val firestore: FirebaseFirestore
): RemoteRepository {
    override suspend fun addRoute(route: Route, points: List<Point>) {

        val routeResponse = RouteResponse(route.routeId,
            route.createdAt,
            route.userId,
            route.name,
            route.description,
            route.sharingType,
            route.distance,
            route.rideTimeMinutes)
        val routeDoc = firestore.collection("routes").document()
        val pointsMap = mapOf("pointsArray" to points.map { PointResponse(it) })
        val pointsDoc = firestore.collection("points").document(routeDoc.id)
        val userDoc = firestore.collection("users").document(route.userId)

        //WRITE BATCHES WORK OFFLINE
        firestore.runBatch { batch ->
            //save RouteResponse
            batch.set(routeDoc, routeResponse)

            //save Points
            batch.set(pointsDoc, pointsMap)

            //assign route to a user
            batch.update(userDoc, "routes", FieldValue.arrayUnion(routeDoc.id))

            //assign route document id to routeId in the document (which is used in the view layer)
            batch.update(routeDoc, "routeId", routeDoc.id)
        }.await()

    }

    override suspend fun getMyRoutes(uid: String, filterData: FilterData): List<Route> {
        val routeResponseList = ArrayList<RouteResponse>()
        val documents: List<DocumentSnapshot>

        var minDistanceMeters: Int? = null
        var maxDistanceMeters: Int? = null

        filterData.minDistanceKm?.let {
            minDistanceMeters =
                if(it == 0) null
                else it * 1000
        }
        filterData.maxDistanceKm?.let {
            maxDistanceMeters =
                if (it == DISTANCE_SLIDER_VALUE_TO.toInt()) null
                else it * 1000
        }

        if(minDistanceMeters==null && maxDistanceMeters==null) {
            documents = firestore
                .collection("routes")
                .whereEqualTo("userId", uid)
                .get().await().documents
        } else if(minDistanceMeters == null && maxDistanceMeters != null) {
            documents = firestore
                .collection("routes")
                .whereEqualTo("userId", uid)
                .whereLessThanOrEqualTo("distance", maxDistanceMeters!!)
                .get().await().documents
        } else if(minDistanceMeters != null && maxDistanceMeters == null){
            documents = firestore
                .collection("routes")
                .whereEqualTo("userId", uid)
                .whereGreaterThanOrEqualTo("distance", minDistanceMeters!!)
                .get().await().documents
        } else {
            documents = firestore
                .collection("routes")
                .whereEqualTo("userId", uid)
                .whereGreaterThanOrEqualTo("distance", minDistanceMeters!!)
                .whereLessThanOrEqualTo("distance", maxDistanceMeters!!)
                .get().await().documents
        }

        for (doc in documents)
            doc.toObject(RouteResponse::class.java)?.let { routeResponseList.add(it) }
        return routeResponseList.map { it.toRoute()}
    }

    override suspend fun getPoints(routeId: String): List<Point> {
        val doc = firestore.collection("points").document(routeId).get().await()
        return doc.toObject(PointDocument::class.java)?.pointsArray?.map { it.toPoint() } ?: throw RuntimeException(
            "no points in the route")
    }

    companion object {
        val LOG = RemoteRepositoryImpl::class.simpleName
    }
}