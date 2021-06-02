package pl.jakubokrasa.bikeroutes.core.di.modules.features

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.sharedroutes.presentation.SharedRoutesRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.sharedroutes.presentation.SharedRoutesViewModel
import pl.jakubokrasa.bikeroutes.features.sharedroutes.domain.GetSharedRoutesUseCase
import pl.jakubokrasa.bikeroutes.features.sharedroutes.domain.GetSharedRoutesWithFilterUseCase

val sharedRoutesModule = module {
    factory { GetSharedRoutesUseCase(get(), get()) }
    factory { GetSharedRoutesWithFilterUseCase(get(), get()) }

    factory { SharedRoutesRecyclerAdapter() }

    viewModel { SharedRoutesViewModel(get(), get(), get(), get()) }

}