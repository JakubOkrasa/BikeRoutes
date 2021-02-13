package pl.jakubokrasa.bikeroutes.features.routerecording.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.jakubokrasa.bikeroutes.core.util.GeoPointConverter
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.PointCached
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

@Database(entities = [RouteCached::class, PointCached::class], version = 1, exportSchema = false)
@TypeConverters(GeoPointConverter::class)
abstract class RoutesDatabase : RoomDatabase() {
    abstract fun routeDao(): RouteDao
}