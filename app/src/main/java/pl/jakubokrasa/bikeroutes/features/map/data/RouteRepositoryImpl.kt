package pl.jakubokrasa.bikeroutes.features.map.data

import androidx.lifecycle.LiveData
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.data.local.PointDao
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.map.presentation.RouteRepository

class RouteRepositoryImpl(private val pointDao: PointDao): RouteRepository {


    override suspend fun insertPoint(geoPoint: GeoPoint) {
        val createdAt = System.currentTimeMillis()
        return pointDao.insertPoint(geoPoint, createdAt)
    }

    override fun getPoints(): LiveData<List<PointCached>> {
        return pointDao.getPoints()
    }

    override suspend fun deletePoints() {
        pointDao.deletePoints()
    }

    override suspend fun addRoute(route: Route) {
        TODO("Not yet implemented")
    }

}