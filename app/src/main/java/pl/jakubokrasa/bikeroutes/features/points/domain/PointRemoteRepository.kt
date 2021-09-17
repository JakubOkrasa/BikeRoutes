package pl.jakubokrasa.bikeroutes.features.points.domain

import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

interface PointRemoteRepository {
    suspend fun getPoints(routeId: String): List<Point>

}