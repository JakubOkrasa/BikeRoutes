package pl.jakubokrasa.bikeroutes.core.di.modules

import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.core.app.presentation.MainViewModel
import pl.jakubokrasa.bikeroutes.core.user.domain.IsUserSignedInUseCase

val appModule = module {
    single { LocalBroadcastManager.getInstance(androidContext())}

    factory { IsUserSignedInUseCase(get()) }

    viewModel { MainViewModel(get(), get(), get(), get()) }
}