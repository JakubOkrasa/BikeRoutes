package pl.jakubokrasa.bikeroutes.features.routerecording.ui

import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route

interface RouteRepository {
    suspend fun getCurrentRoute(): Route

}