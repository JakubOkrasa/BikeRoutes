package pl.jakubokrasa.bikeroutes.core.base.domain

import kotlinx.coroutines.*

abstract class UseCase<out Type, in Params> {
    abstract suspend fun action(params: Params): Type

    operator fun invoke(
        params: Params,
        scope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onResult: (Result<Type>) -> Unit = {}
    ) {
        scope.launch {
            val result = withContext(dispatcher) {
                runCatching { action(params) } //dzieki runcatching, w viemodelu bedzie mozna wywolac onSuccess / onFailure
            }
            onResult(result)
        }
    }
}