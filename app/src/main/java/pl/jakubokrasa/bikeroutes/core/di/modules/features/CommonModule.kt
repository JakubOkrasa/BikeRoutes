package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.common.domain.GetPointsFromRemoteUseCase

val commonModule = module {
    factory { GetPointsFromRemoteUseCase(get()) }
}