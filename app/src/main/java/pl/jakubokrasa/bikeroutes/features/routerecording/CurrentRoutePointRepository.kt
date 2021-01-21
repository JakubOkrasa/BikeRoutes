package pl.jakubokrasa.bikeroutes.features.routerecording

import pl.jakubokrasa.bikeroutes.features.routerecording.domain.CurrentRoutePoint

interface CurrentRoutePointRepository {
    suspend fun getCurrentRoute(): CurrentRoutePoint
}