package pl.jakubokrasa.bikeroutes.features.map.presentation

import android.util.Log
import androidx.lifecycle.*
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.DeleteRouteUseCase
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.*
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteWithPointsDisplayable

class RouteViewModel(
    private val insertPointUseCase: InsertPointUseCase,
    private val getPointsUseCase: GetPointsUseCase,
//    private val markRouteAsNotCurrentUseCase: MarkRouteAsNotCurrentUseCase,
//    private val getMyRoutesUseCase: GetMyRoutesUseCase,
//    private val putRouteSaveDataUseCase: PutRouteSaveDataUseCase,
//    private val insertRouteUseCase: InsertRouteUseCase,
//    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val updateDistanceByPrefsUseCase: UpdateDistanceByPrefsUseCase
) : ViewModel() {


    val points by lazy {
        MutableLiveData<List<PointCached>>()
            .also { getPoints(it) }
    }


    private fun getPoints(pointsLiveData: MutableLiveData<List<PointCached>>) {
        getPointsUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
            result -> result.onSuccess {
                pointsLiveData.value = it.value
        }
            result.onFailure { Log.e(LOG_TAG, "getPoints FAILURE") }

        }
    }



    fun insertPoint(geoPoint: GeoPoint) {
        insertPointUseCase(
            params = geoPoint,
            scope = viewModelScope
        ) {
          result -> result.onSuccess {
            Log.d(LOG_TAG, "point inserted")
        }

            result.onFailure { Log.e(LOG_TAG, "point not inserted") }
        }
    }

//    private fun markRouteAsNotCurrent() {
//        markRouteAsNotCurrentUseCase(
//            params = Unit,
//            scope = viewModelScope
//        ) {
//            result -> result.onSuccess { Log.d(LOG_TAG, "route marked as not current")}
//            result.onFailure { Log.e(LOG_TAG, "route NOT marked as not current") }
//        }
//    }
//
//    fun putRouteSaveData(data: DataRouteSave) {
//        putRouteSaveDataUseCase(
//            params = data,
//            scope = viewModelScope
//        ){
//                result ->
//            result.onSuccess {
//                Log.d(LOG_TAG, "route final data saved")
//                markRouteAsNotCurrent()
//            }
//            result.onFailure { Log.e(LOG_TAG, "route final data save error") }
//        }
//    }
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
                result.onSuccess { Log.d(LOG_TAG, "distance update by prefs OK")}
                result.onFailure { Log.e(LOG_TAG, "distance update by prefs error") }
        }
    }

    companion object {
        val LOG_TAG = RouteViewModel::class.simpleName
    }


}
