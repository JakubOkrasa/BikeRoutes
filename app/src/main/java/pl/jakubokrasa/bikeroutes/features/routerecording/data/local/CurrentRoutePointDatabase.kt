package pl.jakubokrasa.bikeroutes.features.routerecording.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

@Database(entities = [Route::class], version = 1, exportSchema = false)
abstract class CurrentRoutePointDatabase : RoomDatabase() {
    abstract fun currentRoutePointDao();
}