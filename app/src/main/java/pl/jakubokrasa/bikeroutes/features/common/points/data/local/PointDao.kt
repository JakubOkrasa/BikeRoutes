package pl.jakubokrasa.bikeroutes.features.common.points.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import pl.jakubokrasa.bikeroutes.features.common.points.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData

@Dao
interface PointDao {

    @Query("INSERT INTO PointCached(geoPointData, createdAt) VALUES(:geoPointData, :createdAt)")
    suspend fun insertPoint(geoPointData: GeoPointData, createdAt: Long)

    @Query("SELECT * FROM PointCached ORDER BY createdAt")
    fun getPointsLiveData(): LiveData<List<PointCached>>

    @Query("SELECT * FROM PointCached ORDER BY createdAt")
    suspend fun getPoints(): List<PointCached>

    @Query("DELETE FROM PointCached")
    suspend fun deletePoints()
}