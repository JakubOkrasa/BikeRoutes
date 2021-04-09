package pl.jakubokrasa.bikeroutes.features.map.domain.model

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.user.sharingType

data class Route(
    val routeId: Long,
    val name: String,
    val description: String,
    val current: Boolean,
    val distance: Int,
    val sharingType: sharingType,
    var points: List<Point>
) {}