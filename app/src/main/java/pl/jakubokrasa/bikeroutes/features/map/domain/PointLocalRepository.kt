package pl.jakubokrasa.bikeroutes.features.map.domain

import androidx.lifecycle.LiveData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

interface PointLocalRepository {
    suspend fun insertPoint(geoPoint: GeoPointData)

    fun getPoints(): LiveData<List<Point>>
    suspend fun getPoints2(): List<Point>

    suspend fun deletePoints()
}