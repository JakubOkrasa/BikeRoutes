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
    private val myRoutesNavigator: MyRoutesNavigator,
): BaseViewModel() {

    private val _myRoutes by lazy { MutableLiveData<List<RouteDisplayable>>() }
    private val _pointsFromRemote by lazy { MutableLiveData<List<PointDisplayable>>() } //liveEvent could be better here todo (but points can be set too early)
    private val _isFilter by lazy { MutableLiveData<Boolean>() }
    private val _geocodingItem by lazy { LiveEvent<GeocodingItemDisplayable>() }
    private val _segmentPointIndex by lazy { LiveEvent<Int>() }
    override val LOG_TAG: String = MyRoutesViewModel::class.simpleName?: "unknown"

    val pointsFromRemote: LiveData<List<PointDisplayable>> by lazy { _pointsFromRemote }
    val myRoutes: LiveData<List<RouteDisplayable>> by lazy { _myRoutes }
    val isFilter: LiveData<Boolean> by lazy { _isFilter }
    val geocodingItem: LiveData<GeocodingItemDisplayable> by lazy { _geocodingItem }
    val segmentPointIndex: LiveData<Int> by lazy { _segmentPointIndex }


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
                handleSuccess("removeRouteAndNavBack", "Route was removed")
            }
            result.onFailure { handleFailure("removeRouteAndNavBack", "Route wasn't removed") }
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
                handleSuccess("updateRoute", "Route was updated")
            }
            result.onFailure { handleFailure("updateRoute", "Route wasn't updated") }
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
            result.onSuccess { handleSuccess("addSegment") }
            result.onFailure { handleFailure("addSegment", errLog = it.message) }
        }
    }
}