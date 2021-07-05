package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import org.koin.ext.scope
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.domain.*
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable
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
    private val myRoutesNavigator: MyRoutesNavigator,
    private val addPhotoUseCase: AddPhotoUseCase,
    private val getPhotosUseCase: GetPhotosUseCase,
    private val removePhotoUseCase: RemovePhotoUseCase,
): BaseViewModel() {

    private val _myRoutes by lazy { MutableLiveData<List<RouteDisplayable>>() }
    private val _pointsFromRemote by lazy { MutableLiveData<List<PointDisplayable>>() } //liveEvent could be better here todo (but points can be set too early)
    private val _isFilter by lazy { MutableLiveData<Boolean>() }
    private val _photos by lazy { MutableLiveData<List<PhotoInfoDisplayable>>() }
    override val LOG_TAG: String = MyRoutesViewModel::class.simpleName?: "unknown"

    val pointsFromRemote: LiveData<List<PointDisplayable>> by lazy { _pointsFromRemote }
    val myRoutes: LiveData<List<RouteDisplayable>> by lazy { _myRoutes }
    val photos: LiveData<List<PhotoInfoDisplayable>> by lazy { _photos }
    val isFilter: LiveData<Boolean> by lazy { _isFilter }


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
                getPhotos(route.routeId)
            }
            result.onFailure { handleFailure("getPointsFromRemote", errLog = it.message) }
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
            result.onSuccess { handleSuccess("addPhoto", "photo added") }
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

    fun removePhoto(photo: PhotoInfoDisplayable) {
        removePhotoUseCase(
            params = photo.toPhotoInfo(),
            scope = viewModelScope
        ) { result ->
            setIdleState()
            result.onSuccess {
                handleSuccess("removePhoto")
            }
            result.onFailure { handleFailure("removePhoto", errLog = it.message) }
        }
    }
}