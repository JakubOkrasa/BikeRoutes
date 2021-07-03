package pl.jakubokrasa.bikeroutes.features.map.data.remote

import android.net.Uri
import com.google.firebase.firestore.*
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.data.model.PhotoInfoResponse
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.common.domain.model.PhotoInfo
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.myroutes.data.model.PointDocument
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO
import java.io.File
import java.lang.Exception

class RemoteRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storageRef: StorageReference
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

    override suspend fun addPhoto(routeId: String, localPath: String, sharingType: SharingType) {

        //save image to Firebase Cloud Storage
        val uri = Uri.fromFile(File(localPath)) //encode local path (e.g. no polish characters)
        val photoRef = storageRef.child("routes/$routeId/photos/${uri.lastPathSegment}")
        photoRef.putFile(uri).await()

        //save reference to Firestore
        val urlResult = photoRef.downloadUrl.await()
        val photo = PhotoInfoResponse("", routeId, urlResult.toString(), sharingType)
        val photoDoc = firestore.collection("photos").document()
        firestore.runBatch { batch ->
            batch.set(photoDoc, photo)
            batch.update(photoDoc, "photoId", photoDoc.id)
        }.await()
    }


    //============ SHARED ROUTES ===================
    override suspend fun getSharedRoutes(uid: String): List<Route> {
        val routeResponseList = ArrayList<RouteResponse>()
        val documents =
            firestore
                .collection("routes")
//                .whereNotEqualTo("userId", uid) //todo omitted for tests
                .whereEqualTo("sharingType", SharingType.PUBLIC.name)
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
        .whereEqualTo("SharingType", SharingType.PUBLIC.name)
        maxDistanceMeters?.let { query = query.whereLessThanOrEqualTo("distance", it) }
        minDistanceMeters?.let { query = query.whereGreaterThanOrEqualTo("distance", it) }
        val documents = query.get().await()

        for (doc in documents)
            doc.toObject(RouteResponse::class.java)
                .let { routeResponseList.add(it) }
        return routeResponseList.map { it.toRoute()}
    }

    override suspend fun getPhotos(routeId: String): List<PhotoInfo> {
        val photoResponseList = ArrayList<PhotoInfoResponse>()
        val documents = firestore.collection("photos")
            .whereEqualTo("routeId", routeId)
            .get().await().documents

        for (doc in documents)
            doc.toObject(PhotoInfoResponse::class.java)?.let { photoResponseList.add(it) }
        return photoResponseList.map { it.toPhotoInfo() }
    }


    companion object {
        val LOG = RemoteRepositoryImpl::class.simpleName
    }
}