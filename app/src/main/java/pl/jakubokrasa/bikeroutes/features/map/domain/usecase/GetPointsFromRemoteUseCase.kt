package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.features.map.domain.PointRemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class GetPointsFromRemoteUseCase(private val repository: PointRemoteRepository) {
    operator fun invoke(
        routeId: String,
        scope: CoroutineScope,
        onResult: (Result<List<Point>>) -> Unit = {}
    ) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { repository.getPoints(routeId) }
            }
            onResult(result)
        }
    }
}