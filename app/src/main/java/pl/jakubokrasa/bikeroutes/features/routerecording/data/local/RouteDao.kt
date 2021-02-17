package pl.jakubokrasa.bikeroutes.features.routerecording.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteWithPointsCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

@Dao
interface RouteDao {
    @Insert
    suspend fun insertRoute(route: RouteCached)

    @Transaction
    @Query("INSERT INTO PointCached(geoPoint, routeId) VALUES(:geoPoint, (SELECT routeId FROM RouteCached WHERE current=1 LIMIT 1))")
    suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint)

    @Transaction
    @Query("SELECT * from RouteCached JOIN PointCached on RouteCached.routeId=PointCached.routeId WHERE current=1")
    suspend fun getCurrentRoute(): RouteWithPointsCached


//    @Query("DELETE FROM RouteCached")
//    fun deleteCurrentRoute()
}