package pl.jakubokrasa.bikeroutes.features.map.data.local

import androidx.lifecycle.LiveData
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.data.local.PointDao
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRepository

class PointRepositoryImpl(private val pointDao: PointDao): PointRepository {


    override suspend fun insertPoint(geoPoint: GeoPoint) {
        val createdAt = System.currentTimeMillis()
        return pointDao.insertPoint(geoPoint, createdAt)
    }

    override fun getPoints(): LiveData<List<PointCached>> {
        return pointDao.getPoints()
    }

    override suspend fun getPoints2(): List<PointCached> {
        return pointDao.getPoints2()
    }

    override suspend fun deletePoints() {
        pointDao.deletePoints()
    }
}