package pl.jakubokrasa.bikeroutes.features.map.domain

import androidx.lifecycle.LiveData
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

interface LocalRepository {
    suspend fun insertPoint(geoPoint: GeoPointData)

    fun getPoints(): LiveData<List<Point>>
    suspend fun getPoints2(): List<Point>

    suspend fun deletePoints()
}