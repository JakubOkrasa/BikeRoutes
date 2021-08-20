package pl.jakubokrasa.bikeroutes.core.base.platform

import kotlinx.coroutines.*

abstract class Helper<out Type, in Params> {
    abstract suspend fun action(params: Params): Type

    operator fun invoke(
        params: Params,
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        onResult: (Result<Type>) -> Unit = {}
    ) {
        scope.launch {
            val result = withContext(dispatcher) {
                runCatching { action(params) }
            }
            onResult(result)
        }
    }
}