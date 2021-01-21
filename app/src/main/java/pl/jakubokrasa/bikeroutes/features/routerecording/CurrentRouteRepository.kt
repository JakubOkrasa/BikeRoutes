package pl.jakubokrasa.bikeroutes.features.routerecording

import pl.jakubokrasa.bikeroutes.features.routerecording.domain.Route

interface CurrentRouteRepository {
    suspend fun getCurrentRoute(): Route
}