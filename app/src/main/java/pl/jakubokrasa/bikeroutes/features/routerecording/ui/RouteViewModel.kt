package pl.jakubokrasa.bikeroutes.features.routerecording.ui

import android.util.Log
import androidx.lifecycle.*
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.GetCurrentRouteUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.InsertCurrentPointUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.InsertNewRouteUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.MarkRouteAsNotCurrentUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.model.RouteWithPointsDisplayable

class RouteViewModel(
    private val getCurrentRouteUseCase: GetCurrentRouteUseCase,
    private val insertCurrentPointUseCase: InsertCurrentPointUseCase,
    private val markRouteAsNotCurrentUseCase: MarkRouteAsNotCurrentUseCase,
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
    private val insertNewRouteUseCase: InsertNewRouteUseCase) : ViewModel() {
    val route by lazy {
        MutableLiveData<RouteWithPointsDisplayable>()
    }
    private val _myRoutes by lazy {
        MutableLiveData<List<Route>>()
            .also { getMyRoutes(it) }
    }

    val myRoutes: LiveData<List<RouteWithPointsDisplayable>> by lazy { //todo consider what is going on here (from AA)
        _myRoutes.map { myRoutes ->
            myRoutes.map { RouteWithPointsDisplayable(it) }
        }
    }


    private fun getCurrentRoute(currentRouteLiveData: MutableLiveData<RouteWithPointsDisplayable>) {
        getCurrentRouteUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result -> result.onSuccess {
                currentRouteLiveData.value = RouteWithPointsDisplayable(it)
        }
            result.onFailure { Log.e(LOG_TAG, "getCurrentRoute FAILURE") }

        }
    }



    private fun getMyRoutes(routeLiveData: MutableLiveData<List<Route>>) {
        getMyRoutesUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
                result -> result.onSuccess {
                    routeLiveData.value = it
        }
            result.onFailure { Log.e(LOG_TAG, "getMyRoutes FAILURE") }

        }
    }

    fun insertNewRoute(route: Route) {
        insertNewRouteUseCase(
            params = route,
            scope = viewModelScope
        ) {
            result -> result.onSuccess { Log.d(LOG_TAG, "route inserted")}
            result.onFailure { Log.e(LOG_TAG, "route not inserted") }
        }
    }

    fun insertCurrentPoint(geoPoint: GeoPoint) {
        insertCurrentPointUseCase(
            params = geoPoint,
            scope = viewModelScope
        ) {
          result -> result.onSuccess {
            Log.d(LOG_TAG, "point inserted")
            getCurrentRoute(this.route)  //todo nie wiem czy to nie powinno być robione automatycznie przez routeslivedata. Już wiem,
                                        // RouteWithPointsDisplayable nie zawiera listy punktów, więc livedata nie powoduje odświerzenia widoku
                                        //to powoduje że livedata jest tu chyba bezużyteczna
        }

            result.onFailure { Log.e(LOG_TAG, "point not inserted") }
        }
    }

    fun markRouteAsNotCurrent() {
        markRouteAsNotCurrentUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result -> result.onSuccess { Log.d(LOG_TAG, "route marked as not current")}
            result.onFailure { Log.e(LOG_TAG, "route NOT marked as not current") }
        }
    }

    companion object {
        val LOG_TAG = RouteViewModel::class.simpleName
    }


}