package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context
import android.location.LocationManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DividerItemDecoration
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.core.app.domain.IsUserSignedInUseCase
import pl.jakubokrasa.bikeroutes.core.app.presentation.MainViewModel
import pl.jakubokrasa.bikeroutes.features.myroutes.domain.GetMyRoutesWithFilterUseCase

val appModule = module {
    single { LocalBroadcastManager.getInstance(androidContext())}

    factory { IsUserSignedInUseCase(get()) }

    viewModel {MainViewModel(get())}
}