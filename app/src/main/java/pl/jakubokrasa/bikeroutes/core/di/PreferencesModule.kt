package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.BuildConfig
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper

val preferencesModule = module {
    single { androidContext().getSharedPreferences(PREFERENCE_FILE, MODE_PRIVATE)}
    single {get<SharedPreferences>().edit()}

    single { PreferenceHelper(androidContext()) }
}

const val PREFERENCE_FILE = BuildConfig.APPLICATION_ID + ".preferences"