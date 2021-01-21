package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.CurrentRouteRepository

class GetCurrentRouteUseCase(private val repository: CurrentRouteRepository): UseCase<Route, Unit>() {
    override suspend fun action(params: Unit) = repository.getCurrentRoute()
}