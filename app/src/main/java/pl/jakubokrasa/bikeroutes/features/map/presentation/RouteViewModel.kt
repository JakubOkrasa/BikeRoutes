package pl.jakubokrasa.bikeroutes.features.map.presentation

import android.util.Log
import androidx.lifecycle.*
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.DeleteRouteUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.*
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.model.RouteWithPointsDisplayable

class RouteViewModel(
    private val getCurrentRouteUseCase: GetCurrentRouteUseCase,
    private val insertCurrentPointUseCase: InsertCurrentPointUseCase,
    private val markRouteAsNotCurrentUseCase: MarkRouteAsNotCurrentUseCase,
    private val getMyRoutesUseCase: GetMyRoutesUseCase,
    private val putRouteSaveDataUseCase: PutRouteSaveDataUseCase,
    private val insertRouteUseCase: InsertRouteUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val updateDistanceByPrefsUseCase: UpdateDistanceByPrefsUseCase
) : ViewModel() {
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
            result -> result.onSuccess { //todo route -> ..
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
        insertRouteUseCase(
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

    private fun markRouteAsNotCurrent() {
        markRouteAsNotCurrentUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result -> result.onSuccess { Log.d(LOG_TAG, "route marked as not current")}
            result.onFailure { Log.e(LOG_TAG, "route NOT marked as not current") }
        }
    }

    fun putRouteSaveData(data: DataRouteSave) {
        putRouteSaveDataUseCase(
            params = data,
            scope = viewModelScope
        ){
                result ->
            result.onSuccess {
                Log.d(LOG_TAG, "route final data saved")
                markRouteAsNotCurrent()
            }
            result.onFailure { Log.e(LOG_TAG, "route final data save error") }
        }
    }

    fun deleteRoute(route: Route) {
        deleteRouteUseCase(
            params = route,
            scope = viewModelScope
        ){
                result -> result.onSuccess { Log.d(LOG_TAG, "route removed")}
            result.onFailure { Log.e(LOG_TAG, "route removing error") }
        }
    }

    fun updateDistanceByPrefs(geoPoint: GeoPoint) {
        updateDistanceByPrefsUseCase(
            params = geoPoint,
            scope = viewModelScope
        ){
            result ->
                result.onSuccess { Log.d(LOG_TAG, "distance update by prefs OK")}
                result.onFailure { Log.e(LOG_TAG, "distance update by prefs error") }
        }
    }

    companion object {
        val LOG_TAG = RouteViewModel::class.simpleName
    }


}
