package pl.jakubokrasa.bikeroutes.features.routerecording.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

@Dao
interface RouteDao {
//    @Insert
//    suspend fun insertCurrentRoutePoint(currentRoutePoint: Route)

    @Query("SELECT * from RouteCached WHERE  ORDER BY id ASC")
    fun getCurrentRoute(): LiveData<List<Route>>
//
//    @Query("DELETE FROM RouteCached")
//    fun deleteCurrentRoute()
}