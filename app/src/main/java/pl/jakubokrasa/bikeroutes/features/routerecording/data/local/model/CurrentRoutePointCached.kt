package pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.CurrentRoutePoint

@Entity
data class CurrentRoutePointCached(
    @PrimaryKey(autoGenerate = true)
    val id: Long, // not sure if it is number
    var point: GeoPoint
    )
{
    constructor(crp: CurrentRoutePoint)
    : this(crp.id, crp.point)

    fun toCurrentRoutePoint(pointCached: CurrentRoutePointCached): CurrentRoutePoint {
        return CurrentRoutePoint(
            pointCached.id,
            pointCached.point
        )
    }
}