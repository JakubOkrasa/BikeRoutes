package pl.jakubokrasa.bikeroutes.features.map.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import pl.jakubokrasa.bikeroutes.features.map.domain.PointLocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class PointLocalRepositoryImpl(private val pointDao: PointDao): PointLocalRepository {


    override suspend fun insertPoint(geoPoint: GeoPointData) {
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