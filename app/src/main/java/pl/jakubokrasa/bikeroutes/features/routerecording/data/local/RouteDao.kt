package pl.jakubokrasa.bikeroutes.features.routerecording.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

@Dao
interface RouteDao {
//    "INSERT INTO PointCached(geoPoint) VALUES(:point) JOIN"
//    INSERT INTO PointCached(geoPoint) SELECT * FROM PointCached INNER JOIN RouteCached ON PointCached //todo spróbuj dodAC ROUTEid do PointCached; ale wtedy może
                                                                                                        //     todo być redundacja, może lepiej spytaj na slacku
    @Query("INSERT INTO (geoPoint) SELECT * FROM PointCached INNER JOIN RouteCached ON PointCached")
    @Transaction
    suspend fun insertCurrentRoutePoint(point: Route)

    @Query("SELECT * from RouteCached WHERE  ORDER BY id ASC")
    @Transaction
    fun getCurrentRoute(): MutableLiveData<List<Route>>
//
//    @Query("DELETE FROM RouteCached")
//    fun deleteCurrentRoute()
}