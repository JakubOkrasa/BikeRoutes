package pl.jakubokrasa.bikeroutes.core.base.platform

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