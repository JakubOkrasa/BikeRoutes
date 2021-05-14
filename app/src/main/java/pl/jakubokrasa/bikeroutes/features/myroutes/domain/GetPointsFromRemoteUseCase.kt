package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class GetPointsFromRemoteUseCase(private val remoteRepository: RemoteRepository) {
    operator fun invoke(
        routeId: String,
        scope: CoroutineScope,
        onResult: (Result<List<Point>>) -> Unit = {}
    ) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { remoteRepository.getPoints(routeId).sortedBy { it.createdAt } }
            }
            onResult(result)
        }
    }
}