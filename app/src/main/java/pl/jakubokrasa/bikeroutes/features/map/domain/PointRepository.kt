package pl.jakubokrasa.bikeroutes.features.map.domain

import androidx.lifecycle.LiveData
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

interface PointRepository {
    suspend fun insertPoint(geoPoint: GeoPoint)

    fun getPoints(): LiveData<List<PointCached>>

    suspend fun deletePoints()
}