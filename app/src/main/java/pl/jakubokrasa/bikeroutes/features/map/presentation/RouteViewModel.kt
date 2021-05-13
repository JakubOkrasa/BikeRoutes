package pl.jakubokrasa.bikeroutes.features.map.presentation

import android.util.Log
import androidx.lifecycle.*
import com.hadilq.liveevent.LiveEvent
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.*
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetPointsFromRemoteUseCase

class RouteViewModel(
    private val insertPointUseCase: InsertPointUseCase,
    private val getPointsUseCase: GetPointsUseCase,
    private val deletePointsUseCase: DeletePointsUseCase,
    private val saveRouteUseCase: SaveRouteUseCase,
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
//    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val updateDistanceByPrefsUseCase: UpdateDistanceByPrefsUseCase,
    private val getPointsFromRemoteUseCase: GetPointsFromRemoteUseCase,
) : BaseViewModel() {

    private val _myRoutes by lazy { MutableLiveData<List<RouteDisplayable>>() }
    private val _pointsFromRemote by lazy { MutableLiveData<List<PointDisplayable>>() } //liveEvent could be better here todo (but points can be set too early)

    val myRoutes: LiveData<List<RouteDisplayable>> by lazy { _myRoutes }
    val pointsFromRemote: LiveData<List<PointDisplayable>> by lazy { _pointsFromRemote }

    fun getPoints(): LiveData<List<PointDisplayable>> {
        return getPointsUseCase(params = Unit)
            .map { list -> list.map { PointDisplayable(it) } }
    }

    fun insertPoint(geoPoint: GeoPoint) {
        insertPointUseCase(
            params = geoPoint,
            scope = viewModelScope
        ) {
          result -> result.onSuccess {
            getPointsUseCase(params = Unit) // LiveData is not so fast to show points immediately after insert
            handleSuccess("insertPoint")
        }
            result.onFailure { handleFailure("insertPoint") }
        }
    }

    fun saveRoute(dataSaveRoute: DataSaveRoute) {
        setPendingState()
        saveRouteUseCase(
            params = dataSaveRoute,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess { handleSuccess("saveRoute", "Route saved") }
            result.onFailure { handleFailure("saveRoute", "Route not saved") }
        }
    }

    fun deletePoints() {
        deletePointsUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess { handleSuccess("deletePoints") }
            result.onFailure { handleFailure("deletePoints") }
        }
    }

//
//    fun deleteRoute(route: Route) {
//        deleteRouteUseCase(
//            params = route,
//            scope = viewModelScope
//        ){
//                result -> result.onSuccess { Log.d(LOG_TAG, "route removed")}
//            result.onFailure { Log.e(LOG_TAG, "route removing error") }
//        }
//    }
//
    fun updateDistanceByPrefs(geoPoint: GeoPoint) {
        updateDistanceByPrefsUseCase(
            params = geoPoint,
            scope = viewModelScope
        ){
            result ->
                result.onSuccess { handleSuccess("updateDistanceByPrefs") }
                result.onFailure { handleFailure("updateDistanceByPrefs") }
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
                    handleSuccess("getMyRoutes")
                }
                result.onFailure {
                    handleFailure("getMyRoutes", errLog = it.message)
                }
        }
    }

    fun getPointsFromRemote(routeId: String) {
        setPendingState()
        getPointsFromRemoteUseCase(
            routeId = routeId,
            scope = viewModelScope
        ) {
            result ->
            setIdleState()
            result.onSuccess {
                _pointsFromRemote.value = it.map { point -> PointDisplayable(point)}
                handleSuccess("getPointsFromRemote")
            }
            result.onFailure { handleFailure("getPointsFromRemote", errLog = it.message) }
        }
    }

    private fun handleSuccess(methodName: String, msg: String = "") {
        Log.d(LOG_TAG, "onSuccess $methodName")
        if (msg.isNotEmpty())
            showMessage(msg)
    }

    private fun handleFailure(methodName: String, msg: String = "", errLog: String?="") {
        Log.e(LOG_TAG, "onFailure $methodName $errLog")
        if (msg.isNotEmpty())
            showMessage("Error: $msg")
    }

    companion object {
        val LOG_TAG = RouteViewModel::class.simpleName
    }


}
