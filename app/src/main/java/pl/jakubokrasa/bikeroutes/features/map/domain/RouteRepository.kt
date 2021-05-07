package pl.jakubokrasa.bikeroutes.features.map.domain

import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

interface RouteRepository {
    suspend fun addRoute(route: Route)
}