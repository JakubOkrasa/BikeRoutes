package pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.util.GeoPointConverter

@Entity
data class PointCached(
    @PrimaryKey(autoGenerate = true)
    val pointId: Long,
    val routeId: Long,
    @TypeConverters(GeoPointConverter::class)
    val geoPoint: GeoPoint
) {
}