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
    @Query("SELECT * from RouteCached WHERE current=1")
    suspend fun getCurrentRoute(): RouteWithPointsCached

    @Query("SELECT * from RouteCached WHERE current=0")
    suspend fun getMyRoutes(): List<RouteWithPointsCached>

//    @Query("SELECT * from RouteCached WHERE name")
//    suspend fun getRouteByName(routeName: String): RouteWithPointsCached

    //WRITE


    //=============simple operations==============
    @Query("SELECT routeId from RouteCached WHERE current=1")
    suspend fun getCurrentRouteId(): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoute(route: RouteCached)


    @Query("UPDATE RouteCached SET name=:name WHERE routeId=:id")
    suspend fun updateRouteName(id: Long, name: String)

    @Query("UPDATE RouteCached SET description=:desc WHERE routeId=:id")
    suspend fun updateRouteDescription(id: Long, desc: String)

    @Query("UPDATE RouteCached SET distance=:distance WHERE routeId=:id")
    suspend fun updateRouteDistance(id: Long, distance: Int)

    //=============================================

    @Transaction
    @Query("INSERT INTO PointCached(geoPoint, routeId) VALUES(:geoPoint, (SELECT routeId FROM RouteCached WHERE current=1 LIMIT 1))")
    suspend fun insertCurrentRoutePoint(geoPoint: GeoPoint)


    @Query("UPDATE RouteCached SET current=0 WHERE current=1")
    suspend fun markRouteAsNotCurrent()
}