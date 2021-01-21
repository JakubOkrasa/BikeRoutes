package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.jakubokrasa.bikeroutes.features.routerecording.CurrentRouteRepository

class GetCurrentRouteUseCase(private val repository: CurrentRouteRepository) {
    operator fun invoke(
        scope: CoroutineScope,
        onResult: (Result<Route>) -> Unit
    ) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { repository.getCurrentRoute() } //dzieki runcatching, w viemodelu bedzie mozna wywolac onSuccess / onFailure
            }
            onResult(result)
        }
    }
}