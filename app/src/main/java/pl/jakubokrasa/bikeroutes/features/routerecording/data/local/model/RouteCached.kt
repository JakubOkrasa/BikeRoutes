package pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

@Entity
data class RouteCached(
    @PrimaryKey(autoGenerate = true)
    val routeId: Long,
    val current: Boolean
    )
{
//    fun toRoute() = Route(routeId, current)
}