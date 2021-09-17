package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.GetGeocodingItemUseCase
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.FilterData
import pl.jakubokrasa.bikeroutes.features.common.filter.presentation.model.GeocodingItemDisplayable
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.AddPhotoData
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.AddPhotoUseCase
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.GetPhotosUseCase
import pl.jakubokrasa.bikeroutes.features.common.photos.domain.RemovePhotoUseCase
import pl.jakubokrasa.bikeroutes.features.common.photos.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.AddReviewUseCase
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.GetReviewsUseCase
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.RemoveReviewUseCase
import pl.jakubokrasa.bikeroutes.features.common.reviews.domain.UpdateReviewUseCase
import pl.jakubokrasa.bikeroutes.features.common.reviews.presentation.model.ReviewDisplayable
import pl.jakubokrasa.bikeroutes.features.common.routes.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.GetSegmentsUseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.GetSegmentBeginData
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.GetSegmentPointHelper
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model.SegmentDisplayable
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.GetPointsFromRemoteUseCase
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.*
import pl.jakubokrasa.bikeroutes.features.myroutes.navigation.MyRoutesNavigator

class MyRoutesViewModel(
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
    private val getMyRoutesWithFilterUseCase: GetMyRoutesWithFilterUseCase,
    private val getPointsFromRemoteUseCase: GetPointsFromRemoteUseCase,
    private val removeRouteUseCase: RemoveRouteUseCase,
    private val updateRouteUseCase: UpdateRouteUseCase,
    private val getGeocodingItemUseCase: GetGeocodingItemUseCase,
    private val addSegmentUseCase: AddSegmentUseCase,
    private val removeSegmentUseCase: RemoveSegmentUseCase,
    private val getSegmentsUseCase: GetSegmentsUseCase,
    private val addPhotoUseCase: AddPhotoUseCase,
    private val getPhotosUseCase: GetPhotosUseCase,
    private val removePhotoUseCase: RemovePhotoUseCase,
    private val getReviewsUseCase: GetReviewsUseCase,
    private val addReviewUseCase: AddReviewUseCase,
    private val updateReviewUseCase: UpdateReviewUseCase,
    private val removeReviewUseCase: RemoveReviewUseCase,

    private val exportRouteHelper: ExportRouteHelper,
    private val getSegmentPointHelper: GetSegmentPointHelper,

    private val myRoutesNavigator: MyRoutesNavigator,
): BaseViewModel() {

    private val _myRoutes by lazy { MutableLiveData<List<RouteDisplayable>>() }
    private val _isFilter by lazy { MutableLiveData<Boolean>() }
    private val _geocodingItem by lazy { LiveEvent<GeocodingItemDisplayable>() }
	private val _photos by lazy { MutableLiveData<List<PhotoInfoDisplayable>>() }
    private val _photoRemovePos by lazy { LiveEvent<Int>() }
	private val _segmentPointIndex by lazy { LiveEvent<Int>() }
    private val _isSegmentAdded by lazy { LiveEvent<Boolean>() }
    private val _segments by lazy { LiveEvent<List<SegmentDisplayable>>() }
    private val _exportedRouteUri by lazy { LiveEvent<Uri>() }
    private val _reviews by lazy { MutableLiveData<List<ReviewDisplayable>>() }
    override val LOG_TAG: String = MyRoutesViewModel::class.simpleName?: "unknown"

    val myRoutes: LiveData<List<RouteDisplayable>> by lazy { _myRoutes }
    val photos: LiveData<List<PhotoInfoDisplayable>> by lazy { _photos }
    val photoRemovePos: LiveData<Int> by lazy { _photoRemovePos }
    val isFilter: LiveData<Boolean> by lazy { _isFilter }
    val geocodingItem: LiveData<GeocodingItemDisplayable> by lazy { _geocodingItem }
    val segmentPointIndex: LiveData<Int> by lazy { _segmentPointIndex }
    val isSegmentAdded: LiveData<Boolean> by lazy { _isSegmentAdded }
    val segments: LiveData<List<SegmentDisplayable>> by lazy { _segments }
    val exportedRouteUri: LiveData<Uri> by lazy { _exportedRouteUri }
    val reviews: LiveData<List<ReviewDisplayable>> by lazy { _reviews }


    fun removeRouteAndNavBack(route: RouteDisplayable) {
        setPendingState()
        removeRouteUseCase(
            params = route.toRoute(),
            scope = viewModelScope
        ){
                result ->
            setIdleState()
            result.onSuccess {
                myRoutesNavigator.goBack()
                handleSuccess("removeRouteAndNavBack", "Route removed")
            }
            result.onFailure { handleFailure("removeRouteAndNavBack", "Route not removed") }
        }
    }

    fun updateRoute(route: RouteDisplayable) {
        setPendingState()
        updateRouteUseCase(
            params = route.toRoute(),
            scope = viewModelScope
        ){
                result ->
            setIdleState()
            result.onSuccess {
                handleSuccess("updateRoute", "Route updated")
            }
            result.onFailure { handleFailure("updateRoute", "Route not updated") }
        }
    }



    fun getMyRoutesWithFilter(filterData: FilterData) {
        setPendingState()
        getMyRoutesWithFilterUseCase(
            params = filterData,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                _myRoutes.value = it.map { route ->  RouteDisplayable(route) }
                _isFilter.value = true
                handleSuccess("getMyRoutesWithFilter")
            }
            result.onFailure {
                handleFailure("getMyRoutesWithFilter", "couldn't filter routes", errLog = it.message)
            }
        }
    }

    fun getMyRoutes() {
        setPendingState()
        getMyRoutesUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                _myRoutes.value = it.map { route ->  RouteDisplayable(route) }
                _isFilter.value = false
                handleSuccess("getMyRoutes")
            }
            result.onFailure {
                handleFailure("getMyRoutes", "couldn't get routes", errLog = it.message)
            }
        }
    }

    fun getPointsFromRemoteAndOpenRouteDetailsFrg(route: RouteDisplayable) {
        setPendingState()
        getPointsFromRemoteUseCase(
            params = route.routeId,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                handleSuccess("getPointsFromRemote")
                myRoutesNavigator.openRouteDetailsFragment(route, it.map { point -> PointDisplayable(point) })
                getPhotos(route.routeId)
            }
            result.onFailure { handleFailure("getPointsFromRemote", "couldn't display the route", errLog = it.message) }
        }
    }

    fun getGeocodingItem(query: String) {
        getGeocodingItemUseCase(
            params = query,
            scope = viewModelScope
        ) {
            result ->
            result.onSuccess {
                handleSuccess("getGeocodingItem")
                _geocodingItem.value = GeocodingItemDisplayable(it)
            }
            result.onFailure { handleFailure("getGeocodingItem", "couldn't filter by this location", errLog = it.message) }
        }
    }

fun addPhoto(routeId: String, localPath: String, sharingType: SharingType) {
        setPendingState()
        addPhotoUseCase(
            params = AddPhotoData(routeId, localPath, sharingType),
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                getPhotos(routeId)
                handleSuccess("addPhoto", "photo added")
            }
            result.onFailure { handleFailure("addPhoto", "couldn't add the photo", errLog = it.message) }
        }
    }

    fun getPhotos(routeId: String) {
        setPendingState()
        getPhotosUseCase(params = routeId, scope = viewModelScope) { result ->
            setIdleState()
            result.onSuccess { list ->
                _photos.value = list.map { PhotoInfoDisplayable(it) }
                handleSuccess("getPhotos")
            }
            result.onFailure { handleFailure("getPhotos", "couldn't download photos", errLog = it.message) }
        }
    }

    fun removePhoto(photo: PhotoInfoDisplayable, photoPosition: Int) {
        removePhotoUseCase(
            params = photo.toPhotoInfo(),
            scope = viewModelScope
        ) { result ->
            setIdleState()
            result.onSuccess {
                _photoRemovePos.value = photoPosition
                handleSuccess("removePhoto")
            }
            result.onFailure { handleFailure("removePhoto", "could't remove photo", errLog = it.message) }
        }
    }

	fun getSegmentPoint(geoPoint: GeoPoint, points: List<PointDisplayable>, zoomLevel: Double) {
        getSegmentPointHelper(
            params = GetSegmentBeginData(geoPoint, points.map { it.toPointNoCreatedAt() }, zoomLevel),
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess {
                _segmentPointIndex.value = it
                handleSuccess("getSegmentPoint")
            }
            result.onFailure { handleFailure("getSegmentPoint", errLog = it.message) }
        }
    }

	fun addSegment(segmentDisplayable: SegmentDisplayable) {
        setPendingState()
        addSegmentUseCase(
            params = segmentDisplayable.toSegment(),
            scope = viewModelScope
        ) {
            result ->
            setIdleState()
            result.onSuccess {
                _isSegmentAdded.value = true
                handleSuccess("addSegment", "segment added")
            }
            result.onFailure {
                _isSegmentAdded.value = false
                handleFailure("addSegment", "couldn't add the segment", errLog = it.message)
            }
        }
    }

    fun removeSegment(segmentId: String) {
        setPendingState()
        removeSegmentUseCase(
            params = segmentId,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                handleSuccess("removeSegment", "segment removed")
            }
            result.onFailure {
                handleFailure("removeSegment", "couldn't remove the segment", errLog = it.message)
            }
        }
    }

    fun getSegments(routeId: String) {
        setPendingState()
        getSegmentsUseCase(
            params = routeId,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess { list ->
                _segments.value = list.map { SegmentDisplayable(it) }
                handleSuccess("getSegments")
            }
            result.onFailure {
                handleFailure("getSegments", "couldn't get segments", errLog = it.message)
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun exportRoute(route: RouteDisplayable, polyline: Polyline, zoom: Double) {
        setPendingState()
        exportRouteHelper(
            params = ExportRouteData(route.toRoute(), polyline, zoom),
            scope = viewModelScope,
            dispatcher = Dispatchers.Main
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                _exportedRouteUri.value = it
                handleSuccess("exportRoute")
            }
            result.onFailure {
                handleFailure("exportRoute", "couldn't export route", errLog = it.message)
            }
        }
    }

    fun getReviews(routeId: String) {
        setPendingState()
        getReviewsUseCase(
            params = routeId,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess { list ->
                _reviews.value = list.map { ReviewDisplayable(it) }
                handleSuccess("getReviews")
            }
            result.onFailure { handleFailure("getReviews", "couldn't get reviews", errLog = it.message) }
        }
    }

    fun addReview(review: ReviewDisplayable) {
        setPendingState()
        addReviewUseCase(
            params = review.toReview(),
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                getReviews(review.routeId)
                handleSuccess("addReview")
            }
            result.onFailure { handleFailure("addReview", "couldn't add the review", errLog = it.message) }
        }
    }

    fun updateReview(review: ReviewDisplayable) {
        setPendingState()
        updateReviewUseCase(
            params = review.toReview(),
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                handleSuccess("updateReview")
            }
            result.onFailure { handleFailure("updateReview", "couldn't update the review", errLog = it.message) }
        }
    }

    fun removeReview(reviewId: String) {
        setPendingState()
        removeReviewUseCase(
            params = reviewId,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                handleSuccess("removeReview")
            }
            result.onFailure { handleFailure("removeReview", "couldn't remove the review", errLog = it.message) }
        }
    }
    fun navigateBack() {
        myRoutesNavigator.goBack()
    }
}