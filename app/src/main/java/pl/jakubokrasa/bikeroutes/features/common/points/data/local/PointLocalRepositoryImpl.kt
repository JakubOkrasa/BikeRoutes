package pl.jakubokrasa.bikeroutes.features.common.points.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import pl.jakubokrasa.bikeroutes.features.common.points.domain.PointLocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class PointLocalRepositoryImpl(private val pointDao: PointDao): PointLocalRepository {

    override suspend fun insertPoint(geoPoint: GeoPointData) {
        val createdAt = System.currentTimeMillis()
        return pointDao.insertPoint(geoPoint, createdAt)
    }

    override fun getPointsLiveData(): LiveData<List<Point>> {
        return Transformations.map(pointDao.getPointsLiveData()) {
            list -> list.map { it.toPoint() }
        }
    }

    override suspend fun getPoints(): List<Point> {
        return pointDao.getPoints().map { it.toPoint() }
    }

    override suspend fun deletePoints() {
        pointDao.deletePoints()
    }
}