package pl.jakubokrasa.bikeroutes.features.routerecording

import pl.jakubokrasa.bikeroutes.features.routerecording.domain.CurrentRoutePoint

interface CurrentRouteRepository {
    suspend fun getCurrentRoute(): CurrentRoutePoint
}