package pl.jakubokrasa.bikeroutes.features.map.presentation

import android.util.Log
import androidx.lifecycle.*
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.*
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable

class RouteViewModel(
    private val insertPointUseCase: InsertPointUseCase,
    private val getPointsUseCase: GetPointsUseCase,
    private val deletePointsUseCase: DeletePointsUseCase,
    private val saveRouteUseCase: SaveRouteUseCase,
//    private val markRouteAsNotCurrentUseCase: MarkRouteAsNotCurrentUseCase,
//    private val getMyRoutesUseCase: GetMyRoutesUseCase,
//    private val putRouteSaveDataUseCase: PutRouteSaveDataUseCase,
//    private val insertRouteUseCase: InsertRouteUseCase,
//    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val updateDistanceByPrefsUseCase: UpdateDistanceByPrefsUseCase
) : ViewModel() {

    fun getPoints(): LiveData<List<PointDisplayable>> {
        return getPointsUseCase(params = Unit).map { list -> list.map { PointDisplayable(it) } }
    }

    fun insertPoint(geoPoint: GeoPoint) {
        insertPointUseCase(
            params = geoPoint,
            scope = viewModelScope
        ) {
          result -> result.onSuccess {
            getPointsUseCase(params = Unit) // LiveData is not so fast to show points immediately after insert
            Log.d(LOG_TAG, "point inserted")
        }

            result.onFailure { Log.e(LOG_TAG, "point not inserted") }
        }
    }

    fun saveRoute(dataSaveRoute: DataSaveRoute) {
        saveRouteUseCase(
            params = dataSaveRoute,
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess { Log.d(LOG_TAG, "route save OK")}
            result.onFailure { Log.e(LOG_TAG, "route saving error: ${it.message}") }
        }
    }

    fun deletePoints() {
        deletePointsUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess { Log.d(LOG_TAG, "points delete OK")}
            result.onFailure { Log.e(LOG_TAG, "points deleting error") }
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
//    fun putRouteSaveData(data: DataSaveRoute) {
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
