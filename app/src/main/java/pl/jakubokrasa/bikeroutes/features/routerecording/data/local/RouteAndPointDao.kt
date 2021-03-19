package pl.jakubokrasa.bikeroutes.features.routerecording.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteWithPointsCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

@Dao
interface RouteAndPointDao {

    // READ
    @Transaction
    @Query("SELECT * from RouteCached WHERE current=1")
    suspend fun getCurrentRoute(): RouteWithPointsCached

    @Query("SELECT * from RouteCached WHERE current=0")
    suspend fun getMyRoutes(): List<RouteWithPointsCached>


    //WRITE
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: RouteCached)

    @Transaction
    @Query("INSERT INTO PointCached(geoPoint, routeId) VALUES(:geoPoint, (SELECT routeId FROM RouteCached WHERE current=1 LIMIT 1))")
    suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint)


    @Query("UPDATE RouteCached SET current=0 WHERE current=1")
    suspend fun markRouteAsNotCurrent()
}