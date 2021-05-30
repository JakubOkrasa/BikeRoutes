package pl.jakubokrasa.bikeroutes.features.sharedroutes.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetPointsFromRemoteUseCase
import pl.jakubokrasa.bikeroutes.features.sharedroutes.domain.GetSharedRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.sharedroutes.navigation.SharedRoutesNavigator

class SharedRoutesViewModel(
    private val getSharedRoutesUseCase: GetSharedRoutesUseCase,
//    private val getSharedRoutesWithFilterUseCase: GetSharedRoutesWithFilterUseCase,
    private val getPointsFromRemoteUseCase: GetPointsFromRemoteUseCase,
//    private val removeRouteUseCase: RemoveRouteUseCase,
    private val sharedRoutesNavigator: SharedRoutesNavigator,
): BaseViewModel() {
    private val _sharedRoutes by lazy { MutableLiveData<List<RouteDisplayable>>() }
    private val _pointsFromRemote by lazy { MutableLiveData<List<PointDisplayable>>() } //liveEvent could be better here todo (but points can be set too early)
    private val _isFilter by lazy { MutableLiveData<Boolean>() }
    override val LOG_TAG: String = SharedRoutesViewModel::class.simpleName?: "unknown"

    val pointsFromRemote: LiveData<List<PointDisplayable>> by lazy { _pointsFromRemote }
    val sharedRoutes: LiveData<List<RouteDisplayable>> by lazy { _sharedRoutes }
    val isFilter: LiveData<Boolean> by lazy { _isFilter }



//    fun getSharedRoutesWithFilter(filterData: FilterData) {
//        setPendingState()
//        getSharedroutesWithFilterUseCase(
//            filterData = filterData,
//            scope = viewModelScope
//        ) {
//                result ->
//            setIdleState()
//            result.onSuccess {
//                _sharedroutes.value = it.map { route ->  RouteDisplayable(route)}
//                _isFilter.value = true
//                handleSuccess("getSharedRoutesWithFilter")
//            }
//            result.onFailure {
//                handleFailure("getSharedRoutesWithFilter", errLog = it.message)
//            }
//        }
//    }

    fun getSharedRoutes() {
        setPendingState()
        getSharedRoutesUseCase(
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                _sharedRoutes.value = it.map { route ->  RouteDisplayable(route)}
                _isFilter.value = false
                handleSuccess("getSharedRoutes")
            }
            result.onFailure {
                handleFailure("getSharedRoutes", errLog = it.message)
            }
        }
    }

    fun getPointsFromRemoteAndOpenFollowRouteFrg(route: RouteDisplayable) {
        setPendingState()
        getPointsFromRemoteUseCase(
            routeId = route.routeId,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                handleSuccess("getPointsFromRemote")
                sharedRoutesNavigator.openFollowRouteFragment(route, it.map { point -> PointDisplayable(point) })
            }
            result.onFailure { handleFailure("getPointsFromRemote", errLog = it.message) }
        }
    }

    companion object {
        val LOG_TAG = SharedRoutesViewModel::class.simpleName
    }
}