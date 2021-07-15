package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import org.koin.ext.scope
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.features.common.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.common.domain.GetGeocodingItemUseCase
import pl.jakubokrasa.bikeroutes.features.common.domain.GetPointsFromRemoteUseCase
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.GeocodingItemDisplayable
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.domain.*
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.GetSegmentsUseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model.SegmentDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.*
import pl.jakubokrasa.bikeroutes.features.myroutes.navigation.MyRoutesNavigator

class MyRoutesViewModel(
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
    private val getMyRoutesWithFilterUseCase: GetMyRoutesWithFilterUseCase,
    private val getPointsFromRemoteUseCase: GetPointsFromRemoteUseCase,
    private val removeRouteUseCase: RemoveRouteUseCase,
    private val updateRouteUseCase: UpdateRouteUseCase,
    private val getGeocodingItemUseCase: GetGeocodingItemUseCase,
    private val getSegmentPointUseCase: GetSegmentPointUseCase,
    private val addSegmentUseCase: AddSegmentUseCase,
    private val removeSegmentUseCase: RemoveSegmentUseCase,
    private val getSegmentsUseCase: GetSegmentsUseCase,

    private val myRoutesNavigator: MyRoutesNavigator,
    private val addPhotoUseCase: AddPhotoUseCase,
    private val getPhotosUseCase: GetPhotosUseCase,
    private val removePhotoUseCase: RemovePhotoUseCase,
): BaseViewModel() {

    private val _myRoutes by lazy { MutableLiveData<List<RouteDisplayable>>() }
    private val _pointsFromRemote by lazy { MutableLiveData<List<PointDisplayable>>() } //liveEvent could be better here todo (but points can be set too early)
    private val _isFilter by lazy { MutableLiveData<Boolean>() }
    private val _geocodingItem by lazy { LiveEvent<GeocodingItemDisplayable>() }
	private val _photos by lazy { MutableLiveData<List<PhotoInfoDisplayable>>() }
    private val _photoRemovePos by lazy { LiveEvent<Int>() }
	private val _segmentPointIndex by lazy { LiveEvent<Int>() }
    private val _isSegmentAdded by lazy { LiveEvent<Boolean>() }
    private val _segments by lazy { LiveEvent<List<SegmentDisplayable>>() }
    override val LOG_TAG: String = MyRoutesViewModel::class.simpleName?: "unknown"

    val pointsFromRemote: LiveData<List<PointDisplayable>> by lazy { _pointsFromRemote }
    val myRoutes: LiveData<List<RouteDisplayable>> by lazy { _myRoutes }
    val photos: LiveData<List<PhotoInfoDisplayable>> by lazy { _photos }
    val photoRemovePos: LiveData<Int> by lazy { _photoRemovePos }
    val isFilter: LiveData<Boolean> by lazy { _isFilter }
    val geocodingItem: LiveData<GeocodingItemDisplayable> by lazy { _geocodingItem }
    val segmentPointIndex: LiveData<Int> by lazy { _segmentPointIndex }
    val isSegmentAdded: LiveData<Boolean> by lazy { _isSegmentAdded }
    val segments: LiveData<List<SegmentDisplayable>> by lazy { _segments }


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
            filterData = filterData,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                _myRoutes.value = it.map { route ->  RouteDisplayable(route)}
                _isFilter.value = true
                handleSuccess("getMyRoutesWithFilter")
            }
            result.onFailure {
                handleFailure("getMyRoutesWithFilter", errLog = it.message)
            }
        }
    }

    fun getMyRoutes() {
        setPendingState()
        getMyRoutesUseCase(
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                _myRoutes.value = it.map { route ->  RouteDisplayable(route)}
                _isFilter.value = false
                handleSuccess("getMyRoutes")
            }
            result.onFailure {
                handleFailure("getMyRoutes", errLog = it.message)
            }
        }
    }

    fun getPointsFromRemoteAndOpenRouteDetailsFrg(route: RouteDisplayable) {
        setPendingState()
        getPointsFromRemoteUseCase(
            routeId = route.routeId,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                handleSuccess("getPointsFromRemote")
                myRoutesNavigator.openRouteDetailsFragment(route, it.map { point -> PointDisplayable(point) })
                getPhotos(route.routeId)
            }
            result.onFailure { handleFailure("getPointsFromRemote", errLog = it.message) }
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
            result.onFailure { handleFailure("getGeocodingItem", errLog = it.message) }
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
            result.onFailure { handleFailure("addPhoto", errLog = it.message) }
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
            result.onFailure { handleFailure("getPhotos", errLog = it.message) }
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
            result.onFailure { handleFailure("removePhoto", errLog = it.message) }
        }
    }

	fun getSegmentPoint(geoPoint: GeoPoint, points: List<PointDisplayable>, zoomLevel: Double) {
        getSegmentPointUseCase(
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
                handleFailure("addSegment", errLog = it.message)
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
                handleFailure("removeSegment", errLog = it.message)
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
                handleFailure("getSegments", errLog = it.message)
            }
        }
    }

    fun navigateBack() {
        myRoutesNavigator.goBack()
    }
}