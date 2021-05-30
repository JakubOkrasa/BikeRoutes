package pl.jakubokrasa.bikeroutes.core.base.platform

import androidx.lifecycle.viewModelScope
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.FilterData
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetPointsFromRemoteUseCase

//abstract class RoutesBaseViewModel<T: Any>(
//    private val getPointsFromRemoteUseCase: GetPointsFromRemoteUseCase,
//): BaseViewModel() {
//    abstract val navigator: T
//
//    fun getPointsFromRemoteAndOpenFollowRouteFrg(route: RouteDisplayable) {
//        setPendingState()
//        getPointsFromRemoteUseCase(
//            routeId = route.routeId,
//            scope = viewModelScope
//        ) {
//                result ->
//            setIdleState()
//            result.onSuccess {
//                handleSuccess("getPointsFromRemote")
//                navigator.openFollowRouteFragment(route, it.map { point -> PointDisplayable(point) })
//            }
//            result.onFailure { handleFailure("getPointsFromRemote", errLog = it.message) }
//        }
//    }
//}