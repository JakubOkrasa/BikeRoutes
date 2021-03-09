package pl.jakubokrasa.bikeroutes.features.routerecording.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.jakubokrasa.bikeroutes.core.util.GeoPointConverter
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached

@Database(entities = [RouteCached::class, PointCached::class], version = 4, exportSchema = false)
@TypeConverters(GeoPointConverter::class)
abstract class RouteAndPointDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteAndPointDao
}