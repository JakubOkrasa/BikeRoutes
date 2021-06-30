package pl.jakubokrasa.bikeroutes.features.map.data.remote

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.util.enums.sharingType
import pl.jakubokrasa.bikeroutes.features.common.data.GeocodingAPI
import pl.jakubokrasa.bikeroutes.features.common.domain.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.common.domain.model.GeocodingItem
import pl.jakubokrasa.bikeroutes.features.common.segments.data.model.SegmentResponse
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.myroutes.data.model.PointDocument
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO

class RemoteRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val api: GeocodingAPI
): RemoteRepository {
    override suspend fun addRoute(route: Route, points: List<Point>) {

        val routeResponse = RouteResponse(route)
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

    override suspend fun getMyRoutes(uid: String): List<Route> {
        val routeResponseList = ArrayList<RouteResponse>()
        val documents =
            firestore
            .collection("routes")
            .whereEqualTo("userId", uid)
            .get().await().documents

        for (doc in documents)
            doc.toObject(RouteResponse::class.java)?.let { routeResponseList.add(it) }

        return routeResponseList.map { it.toRoute()}
    }

    override suspend fun getMyRoutesWithFilter(uid: String, filterData: FilterData): List<Route> {
        val routeResponseList = ArrayList<RouteResponse>()
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

        var query = firestore.collection("routes")
            .whereEqualTo("userId", uid)
        maxDistanceMeters?.let { query = query.whereLessThanOrEqualTo("distance", it) }
        minDistanceMeters?.let { query = query.whereGreaterThanOrEqualTo("distance", it) }
        val documents = query.get().await()

        for (doc in documents)
            doc.toObject(RouteResponse::class.java)
                .let { routeResponseList.add(it) }


        return routeResponseList.map { it.toRoute()}
    }

    override suspend fun updateRoute(route: Route) {
        val routeResponse = RouteResponse(route)
        firestore
            .collection("routes")
            .document(route.routeId)
            .set(routeResponse)
    }

    override suspend fun deleteRoute(route: Route) {
        val routeDoc = firestore.collection("routes").document(route.routeId)
        val pointsDoc = firestore.collection("points").document(route.routeId)
        val userDoc = firestore.collection("users").document(route.userId)

        firestore.runBatch { batch ->
            batch.delete(routeDoc)
            batch.delete(pointsDoc)
            batch.update(userDoc, "routes", FieldValue.arrayRemove(route.routeId))
        }.await()
    }

    override suspend fun getPoints(routeId: String): List<Point> {
        val doc = firestore.collection("points").document(routeId).get().await()
        return doc.toObject(PointDocument::class.java)?.pointsArray?.map { it.toPoint() } ?: throw RuntimeException(
            "no points in the route")
    }

    override suspend fun addSegment(segment: Segment) {
        val segmentDoc = firestore.collection("segments").document()

        firestore.runBatch { batch ->
            batch.set(segmentDoc, SegmentResponse(segment))
            batch.update(segmentDoc, "segmentId", segmentDoc.id)
        }.await()
    }

    //============ SHARED ROUTES ===================
    override suspend fun getSharedRoutes(uid: String): List<Route> {
        val routeResponseList = ArrayList<RouteResponse>()
        val documents =
            firestore
                .collection("routes")
//                .whereNotEqualTo("userId", uid) //todo omitted for tests
                .whereEqualTo("sharingType", sharingType.PUBLIC.name)
                .get().await().documents

        for (doc in documents)
            doc.toObject(RouteResponse::class.java)?.let { routeResponseList.add(it) }
        return routeResponseList.map { it.toRoute()}
    }


    override suspend fun getSharedRoutesWithFilter(uid: String, filterData: FilterData): List<Route> {
        val routeResponseList = ArrayList<RouteResponse>()
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

        var query = firestore.collection("routes")
        // .whereNotEqualTo("userId", uid) //todo omitted for tests
        .whereEqualTo("sharingType", sharingType.PUBLIC.name)
        maxDistanceMeters?.let { query = query.whereLessThanOrEqualTo("distance", it) }
        minDistanceMeters?.let { query = query.whereGreaterThanOrEqualTo("distance", it) }
        val documents = query.get().await()

        for (doc in documents)
            doc.toObject(RouteResponse::class.java)
                .let { routeResponseList.add(it) }
        return routeResponseList.map { it.toRoute()}
    }

    override suspend fun getGeocodingItem(query: String): GeocodingItem {
        return api.getGeocodingItem(query)[0].toGeocodingItem()
    }




    companion object {
        val LOG = RemoteRepositoryImpl::class.simpleName
    }
}