package pl.jakubokrasa.bikeroutes.features.routerecording.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.GetCurrentRouteUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.InsertCurrentPointUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.InsertNewRouteUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.MarkRouteAsNotCurrentUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.model.RouteDisplayable

class RecordRouteViewModel(
    private val getCurrentRouteUseCase: GetCurrentRouteUseCase,
    private val insertCurrentPointUseCase: InsertCurrentPointUseCase,
    private val markRouteAsNotCurrentUseCase: MarkRouteAsNotCurrentUseCase,
    private val insertNewRouteUseCase: InsertNewRouteUseCase) : ViewModel() {
    val route by lazy {
        MutableLiveData<RouteDisplayable>()
    }

    private fun getCurrentRoute(routesLiveData: MutableLiveData<RouteDisplayable>) {
        getCurrentRouteUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result -> result.onSuccess {
                routesLiveData.value = RouteDisplayable(it)
        }
            result.onFailure { Log.e(LOG_TAG, "getCurrentRoute FAILURE") }

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
                                        // RouteDisplayable nie zawiera listy punktów, więc livedata nie powoduje odświerzenia widoku
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
        val LOG_TAG = RecordRouteViewModel::class.simpleName
    }


}