package pl.jakubokrasa.bikeroutes.features.map.data.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import android.net.Uri
import com.google.firebase.firestore.*
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.data.GeocodingAPI
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.common.domain.model.GeocodingItem
import pl.jakubokrasa.bikeroutes.features.common.data.model.PhotoInfoResponse
import pl.jakubokrasa.bikeroutes.features.common.domain.model.PhotoInfo
import pl.jakubokrasa.bikeroutes.features.common.segments.data.model.SegmentResponse
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.myroutes.data.model.PointDocument
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO
import pl.jakubokrasa.bikeroutes.features.reviews.data.model.ReviewResponse
import pl.jakubokrasa.bikeroutes.features.reviews.domain.model.Review
import java.io.File
import java.lang.Exception

class RemoteRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val api: GeocodingAPI,
	private val storageRef: StorageReference
): RemoteRepository {
    override suspend fun addRoute(route: Route, points: List<Point>) {

        val routeResponse = RouteResponse(route)
        val routeDoc = firestore.collection("routes").document()
        val pointsMap = mapOf("pointsArray" to points.map { PointResponse(it) })
        val pointsDoc = firestore.collection("points").document(routeDoc.id)

        //WRITE BATCHES WORK OFFLINE
        firestore.runBatch { batch ->
            //save RouteResponse
            batch.set(routeDoc, routeResponse)

            //save Points
            batch.set(pointsDoc, pointsMap)

            //assign route document id to routeId in the document (which is used in the presentation layer)
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
            .await()
    }

    override suspend fun deleteRoute(route: Route) {
        val routeDoc = firestore.collection("routes").document(route.routeId)
        val pointsDoc = firestore.collection("points").document(route.routeId)

        firestore.runBatch { batch ->
            batch.delete(routeDoc)
            batch.delete(pointsDoc)
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
        val photoName = "photo_${System.currentTimeMillis()}"
        val photoRef = storageRef.child("routes/$routeId/photos/${photoName}")
        photoRef.putFile(uri).await()

        //save reference to Firestore
        val urlResult = photoRef.downloadUrl.await()
        val photo = PhotoInfoResponse("", routeId, urlResult.toString(), sharingType, photoName)
        val photoDoc = firestore.collection("photos").document()
        firestore.runBatch { batch ->
            batch.set(photoDoc, photo)
            batch.update(photoDoc, "photoId", photoDoc.id)
            batch.update(photoDoc, "name", photoName)
        }.await()
    }

    override suspend fun removePhoto(photo: PhotoInfo) {

        //remove from Cloud Storage
        val photoResponse = PhotoInfoResponse(photo)
        val photoRef = storageRef.child("routes/${photoResponse.routeId}/photos/${photoResponse.name}")
        photoRef.delete().await()

        //remove from Firestore
        firestore.collection("photos")
            .document(photoResponse.photoId)
            .delete().await()

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

    //============ SHARED ROUTES ===================
    override suspend fun getSharedRoutes(uid: String): List<Route> {
        val routeResponseList = ArrayList<RouteResponse>()
        val documents =
            firestore
                .collection("routes")
                .whereNotEqualTo("userId", uid)
                .whereIn("sharingType", listOf(SharingType.PUBLIC.name, SharingType.PUBLIC_WITH_PRIVATE_PHOTOS.name))
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
            .whereNotEqualTo("userId", uid)
            .whereIn("sharingType", listOf(SharingType.PUBLIC.name, SharingType.PUBLIC_WITH_PRIVATE_PHOTOS.name))
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

	override suspend fun getPhotos(routeId: String): List<PhotoInfo> {
        val photoResponseList = ArrayList<PhotoInfoResponse>()
        val documents = firestore.collection("photos")
            .whereEqualTo("routeId", routeId)
            .get().await().documents

        for (doc in documents)
            doc.toObject(PhotoInfoResponse::class.java)?.let { photoResponseList.add(it) }
        return photoResponseList.map { it.toPhotoInfo() }
    }

	override suspend fun getSegments(routeId: String): List<Segment> {
        return firestore.collection("segments")
            .whereEqualTo("routeId", routeId)
            .get()
            .await()
            .map { doc -> doc.toObject(SegmentResponse::class.java).toSegment() }
    }

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


    companion object {
        val LOG = RemoteRepositoryImpl::class.simpleName
    }
}