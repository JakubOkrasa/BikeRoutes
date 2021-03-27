package pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.user.sharingType
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

@Entity
data class RouteCached(
    @PrimaryKey(autoGenerate = true)
    val routeId: Long,
    val name: String,
    val description: String,
    val current: Boolean,
    val distance: Int,
    val sharingType: sharingType,
    )
{
    constructor(route: Route): this (
        routeId = route.routeId,
        name = route.name,
        description = route.description,
        current = route.current,
        distance = route.distance,
        sharingType = route.sharingType
    )
}