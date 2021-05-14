package pl.jakubokrasa.bikeroutes.features.map.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.domain.LocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class LocalRepositoryImpl(private val pointDao: PointDao): LocalRepository {


    override suspend fun insertPoint(geoPoint: GeoPoint) {
        val createdAt = System.currentTimeMillis()
        return pointDao.insertPoint(geoPoint, createdAt)
    }

    override fun getPoints(): LiveData<List<Point>> {
        return Transformations.map(pointDao.getPoints()) {
            list -> list.map { it.toPoint() }
        }
    }

    override suspend fun getPoints2(): List<Point> {
        return pointDao.getPoints2().map { it.toPoint() }
    }

    override suspend fun deletePoints() {
        pointDao.deletePoints()
    }
}