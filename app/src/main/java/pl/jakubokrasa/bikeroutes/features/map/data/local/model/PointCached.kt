package pl.jakubokrasa.bikeroutes.features.map.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.osmdroid.util.GeoPoint

@Entity
data class PointCached(
    @PrimaryKey(autoGenerate = true)
    val pointId: Long,
    val routeId: Long,
    val geoPoint: GeoPoint,
    val createdAt: Long
) {
}