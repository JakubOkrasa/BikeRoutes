package pl.jakubokrasa.bikeroutes.features.points.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

@Entity
data class PointCached(
    @PrimaryKey(autoGenerate = true)
    val pointId: Long,
    val geoPointData: GeoPointData,
    val createdAt: Long
) {
    fun toPoint() = Point(
        this.pointId,
        this.geoPointData,
        this.createdAt,
        )
}