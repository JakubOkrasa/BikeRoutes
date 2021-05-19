package pl.jakubokrasa.bikeroutes.features.map.presentation

import android.util.Log
import androidx.lifecycle.*
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseViewModel
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.*
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable

class MapViewModel(
    private val insertPointUseCase: InsertPointUseCase,
    private val getPointsUseCase: GetPointsUseCase,
    private val deletePointsUseCase: DeletePointsUseCase,
    private val saveRouteUseCase: SaveRouteUseCase,
//    private val deleteRouteUseCase: DeleteRouteUseCase,
    private val updateDistanceByPrefsUseCase: UpdateDistanceByPrefsUseCase,

) : BaseViewModel() {

    //=========MAP=================


    fun getPoints(): LiveData<List<PointDisplayable>> {
        return getPointsUseCase(params = Unit)
            .map { list -> list.map { PointDisplayable(it) } }
    }

    fun insertPoint(geoPoint: GeoPoint) {
        insertPointUseCase(
            params = geoPoint,
            scope = viewModelScope
        ) {
                result -> result.onSuccess {
            getPointsUseCase(params = Unit) // LiveData is not so fast to show points immediately after insert
            handleSuccess("insertPoint")
        }
            result.onFailure { handleFailure("insertPoint") }
        }
    }

    fun saveRoute(dataSaveRoute: DataSaveRoute) {
        setPendingState()
        saveRouteUseCase(
            params = dataSaveRoute,
            scope = viewModelScope
        ) {
                result ->
            setIdleState()
            result.onSuccess { handleSuccess("saveRoute", "Route saved") }
            result.onFailure { handleFailure("saveRoute", "Route not saved") }
        }
    }

    fun deletePoints() {
        deletePointsUseCase(
            params = Unit,
            scope = viewModelScope
        ) {
                result ->
            result.onSuccess { handleSuccess("deletePoints") }
            result.onFailure { handleFailure("deletePoints") }
        }
    }

    fun updateDistanceByPrefs(geoPoint: GeoPoint) {
        updateDistanceByPrefsUseCase(
            params = geoPoint,
            scope = viewModelScope
        ){
                result ->
            result.onSuccess { handleSuccess("updateDistanceByPrefs") }
            result.onFailure { handleFailure("updateDistanceByPrefs") }
        }
    }

    private fun handleSuccess(methodName: String, msg: String = "") {
        Log.d(LOG_TAG, "onSuccess $methodName")
        if (msg.isNotEmpty())
            showMessage(msg)
    }

    private fun handleFailure(methodName: String, msg: String = "", errLog: String?="") {
        Log.e(LOG_TAG, "onFailure $methodName $errLog")
        if (msg.isNotEmpty())
            showMessage("Error: $msg")
    }

    companion object {
        val LOG_TAG = MapViewModel::class.simpleName
    }


}
