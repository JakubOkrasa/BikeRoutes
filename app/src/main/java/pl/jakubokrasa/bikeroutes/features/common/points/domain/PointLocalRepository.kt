package pl.jakubokrasa.bikeroutes.features.common.points.domain

import androidx.lifecycle.LiveData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

interface PointLocalRepository {
    suspend fun insertPoint(geoPoint: GeoPointData)

    fun getPointsLiveData(): LiveData<List<Point>>
    suspend fun getPoints(): List<Point>

    suspend fun deletePoints()
}