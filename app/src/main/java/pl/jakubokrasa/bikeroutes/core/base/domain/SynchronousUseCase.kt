package pl.jakubokrasa.bikeroutes.core.base.domain

abstract class SynchronousUseCase<out Type, in Params> {
    abstract fun action(params: Params): Type

    operator fun invoke(params: Params): Type {
        return action(params)
    }
}