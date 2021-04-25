package pl.jakubokrasa.bikeroutes.features.map.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.jakubokrasa.bikeroutes.core.util.converter.EnumConverter
import pl.jakubokrasa.bikeroutes.core.util.converter.GeoPointConverter
import pl.jakubokrasa.bikeroutes.features.map.data.local.model.PointCached

@Database(entities = [PointCached::class], version = 7, exportSchema = false)
@TypeConverters(GeoPointConverter::class, EnumConverter::class)
abstract class PointDatabase : RoomDatabase() {
    abstract fun routeDao(): PointDao
}