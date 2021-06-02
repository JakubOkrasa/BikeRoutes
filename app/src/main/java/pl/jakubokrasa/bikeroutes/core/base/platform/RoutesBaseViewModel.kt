package pl.jakubokrasa.bikeroutes.core.base.platform

//abstract class RoutesBaseViewModel<T: Any>(
//    private val getPointsFromRemoteUseCase: GetPointsFromRemoteUseCase,
//): BaseViewModel() {
//    abstract val navigator: T
//
//    fun getPointsFromRemoteAndOpenRouteDetailsFrg(route: RouteDisplayable) {
//        setPendingState()
//        getPointsFromRemoteUseCase(
//            routeId = route.routeId,
//            scope = viewModelScope
//        ) {
//                result ->
//            setIdleState()
//            result.onSuccess {
//                handleSuccess("getPointsFromRemote")
//                navigator.openRouteDetailsFragment(route, it.map { point -> PointDisplayable(point) })
//            }
//            result.onFailure { handleFailure("getPointsFromRemote", errLog = it.message) }
//        }
//    }
//}