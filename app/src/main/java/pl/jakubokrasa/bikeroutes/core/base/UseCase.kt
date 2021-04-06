package pl.jakubokrasa.bikeroutes.core.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class UseCase<out Type, in Params> {
    abstract suspend fun action(params: Params): Type

    operator fun invoke(
        params: Params,
        scope: CoroutineScope,
        onResult: (Result<Type>) -> Unit = {}
    ) {
        scope.launch {
            val result = withContext(Dispatchers.IO) {
                runCatching { action(params) } //dzieki runcatching, w viemodelu bedzie mozna wywolac onSuccess / onFailure
            }
            onResult(result)
        }
    }
}