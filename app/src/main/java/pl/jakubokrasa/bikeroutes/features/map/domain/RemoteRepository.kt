package pl.jakubokrasa.bikeroutes.features.map.domain

import pl.jakubokrasa.bikeroutes.features.common.data.model.GeocodingItemResponse
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.common.domain.model.PhotoInfo
import pl.jakubokrasa.bikeroutes.features.common.domain.model.GeocodingItem
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.reviews.domain.model.Review

interface RemoteRepository {

    //MY ROUTES
    suspend fun addRoute(route: Route, points: List<Point>)

    suspend fun getMyRoutes(uid: String): List<Route>
    suspend fun getMyRoutesWithFilter(uid: String, filterData: FilterData): List<Route>

    suspend fun updateRoute(route: Route)

    suspend fun deleteRoute(route: Route)

    suspend fun getPoints(routeId: String): List<Point>

    suspend fun addPhoto(routeId: String, localPath: String, sharingType: SharingType)
    suspend fun removePhoto(photoInfo: PhotoInfo)

	suspend fun addSegment(segment: Segment)

    suspend fun removeSegment(segmentId: String)

    //SHARED ROUTES
    suspend fun getSharedRoutes(uid: String): List<Route>
    suspend fun getSharedRoutesWithFilter(uid: String, filterData: FilterData): List<Route>

    //COMMON
	suspend fun getPhotos(routeId: String): List<PhotoInfo>

	suspend fun getSegments(routeId: String): List<Segment>

    suspend fun getReviews(routeId: String): List<Review>
    suspend fun addReview(review: Review)
    suspend fun updateReview(review: Review)
    suspend fun removeReview(reviewId: String)

}