package pl.jakubokrasa.bikeroutes.features.routerecording.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.GetCurrentRouteUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.InsertCurrentPointUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.model.RouteDisplayable

class RecordRouteViewModel(private val getCurrentRouteUseCase: GetCurrentRouteUseCase, private val insertCurrentPointUseCase: InsertCurrentPointUseCase) : ViewModel() {
    val routes by lazy {
        MutableLiveData<RouteDisplayable>()
            .also { getCurrentRoute(it) }
    }

    private fun getCurrentRoute(routesLiveData: MutableLiveData<RouteDisplayable>) {
        getCurrentRouteUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result -> result.onSuccess {
                routesLiveData.value = RouteDisplayable(it)
        }
        }
    }

    fun insertCurrentPoint(geoPoint: GeoPoint) {
        insertCurrentPointUseCase(
            params = geoPoint,
            scope = viewModelScope
        ) {
          result -> result.onSuccess {
              Log.d(LOG_TAG, "point inserted")
        }
        }
    }

    companion object {
        val LOG_TAG = RecordRouteViewModel::class.simpleName
    }


}