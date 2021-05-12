package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.RouteRepository

class GetPointsFromRemoteUseCase(private val routeRepository: RouteRepository) {
    operator fun invoke(
        routeId: String,
        scope: CoroutineScope,
        onResult: (Result<List<PointResponse>>) -> Unit = {}
    ) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { routeRepository.getPoints(routeId) }
            }
            onResult(result)
        }
    }
}