package pl.jakubokrasa.bikeroutes.core.di

import androidx.activity.result.contract.ActivityResultContracts
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.core.app.domain.IsUserSignedInUseCase
import pl.jakubokrasa.bikeroutes.core.app.presentation.MainViewModel
import pl.jakubokrasa.bikeroutes.core.util.AppUtil

val appModule = module {
    single { LocalBroadcastManager.getInstance(androidContext())}
    single(createdAtStart = true) { AppUtil(get()) }

    factory { IsUserSignedInUseCase(get()) }

    viewModel { MainViewModel(get(), get(), get(), get()) }
}