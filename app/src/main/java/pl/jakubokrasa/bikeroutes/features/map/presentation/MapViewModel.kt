package pl.jakubokrasa.bikeroutes.features.map.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseViewModel
import pl.jakubokrasa.bikeroutes.features.common.points.domain.DeletePointsUseCase
import pl.jakubokrasa.bikeroutes.features.common.points.domain.GetPointsFromLocalSyncUseCase
import pl.jakubokrasa.bikeroutes.features.common.points.domain.InsertPointUseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.*
import pl.jakubokrasa.bikeroutes.features.common.points.presentation.model.PointDisplayable

class MapViewModel(
    private val insertPointUseCase: InsertPointUseCase,
    private val getPointsFromLocalSyncUseCase: GetPointsFromLocalSyncUseCase,
    private val deletePointsUseCase: DeletePointsUseCase,
    private val saveRouteUseCase: SaveRouteUseCase,
    private val updateDistanceHelper: UpdateDistanceHelper
) : BaseViewModel() {

    override val LOG_TAG: String = MapViewModel::class.simpleName ?: "unknown"

    fun getPoints(): LiveData<List<PointDisplayable>> {
        return getPointsFromLocalSyncUseCase(params = Unit)
            .map { list -> list.map { PointDisplayable(it) } }
    }

    fun insertPoint(geoPoint: GeoPointData) {
        insertPointUseCase(
            params = geoPoint,
            scope = viewModelScope
        ) {
                result -> result.onSuccess {
            getPointsFromLocalSyncUseCase(params = Unit) // LiveData is not so fast to show points immediately after insert
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

    fun updateDistance(geoPoint: GeoPoint) {
        updateDistanceHelper(
            params = geoPoint,
            scope = viewModelScope
        ){
                result ->
            result.onSuccess { handleSuccess("updateDistance") }
            result.onFailure { handleFailure("updateDistance") }
        }
    }

}
