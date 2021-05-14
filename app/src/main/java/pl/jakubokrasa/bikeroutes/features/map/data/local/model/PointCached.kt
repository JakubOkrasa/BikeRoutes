package pl.jakubokrasa.bikeroutes.features.map.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

@Entity
data class PointCached(
    @PrimaryKey(autoGenerate = true)
    val pointId: Long,
    val geoPoint: GeoPoint,
    val createdAt: Long
) {
    fun toPoint() = Point(
        this.pointId,
        this.geoPoint,
        this.createdAt,
        difficultTerrainStart = false,
        difficultTerrainEnd = false,
        )
}