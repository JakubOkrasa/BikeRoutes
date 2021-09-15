package pl.jakubokrasa.bikeroutes.features.sharedroutes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hadilq.liveevent.LiveEvent
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseViewModel
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.GetGeocodingItemUseCase
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.FilterData
import pl.jakubokrasa.bikeroutes.features.common.filter.presentation.model.GeocodingItemDisplayable
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.GetPointsFromRemoteUseCase
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.common.routes.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.sharedroutes.domain.GetSharedRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.sharedroutes.domain.GetSharedRoutesWithFilterUseCase
import pl.jakubokrasa.bikeroutes.features.sharedroutes.navigation.SharedRoutesNavigator

class SharedRoutesViewModel(
    private val getSharedRoutesUseCase: GetSharedRoutesUseCase,
    private val getSharedRoutesWithFilterUseCase: GetSharedRoutesWithFilterUseCase,
    private val getPointsFromRemoteUseCase: GetPointsFromRemoteUseCase,
    private val getGeocodingItemUseCase: GetGeocodingItemUseCase,
    private val sharedRoutesNavigator: SharedRoutesNavigator,
): BaseViewModel() {

    private val _sharedRoutes by lazy { MutableLiveData<List<RouteDisplayable>>() }
    private val _isFilter by lazy { MutableLiveData<Boolean>() }
    private val _geocodingItem by lazy { LiveEvent<GeocodingItemDisplayable>() }
    override val LOG_TAG: String = SharedRoutesViewModel::class.simpleName?: "unknown"

    val sharedRoutes: LiveData<List<RouteDisplayable>> by lazy { _sharedRoutes }
    val isFilter: LiveData<Boolean> by lazy { _isFilter }
    val geocodingItem: LiveData<GeocodingItemDisplayable> by lazy { _geocodingItem }

    fun getSharedRoutesWithFilter(filterData: FilterData) {
        setPendingState()
        getSharedRoutesWithFilterUseCase(
            params = filterData,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                _sharedRoutes.value = it.map { route ->  RouteDisplayable(route) }
                _isFilter.value = true
                handleSuccess("getSharedRoutesWithFilter")
            }
            result.onFailure {
                handleFailure("getSharedRoutesWithFilter", "couldn't filter the routes", errLog = it.message)
            }
        }
    }

    fun getSharedRoutes() {
        setPendingState()
        getSharedRoutesUseCase(
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess {
                _sharedRoutes.value = it.map { route ->  RouteDisplayable(route) }
                _isFilter.value = false
                handleSuccess("getSharedRoutes")
            }
            result.onFailure {
                handleFailure("getSharedRoutes", "couldn't get routes", errLog = it.message)
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
                sharedRoutesNavigator.openRouteDetailsFragment(route, it.map { point -> PointDisplayable(point) })
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
            result.onFailure { handleFailure("getGeocodingItem", "couldn't filter by location", errLog = it.message) }
        }
    }

    companion object {
        val LOG_TAG = SharedRoutesViewModel::class.simpleName
    }
}