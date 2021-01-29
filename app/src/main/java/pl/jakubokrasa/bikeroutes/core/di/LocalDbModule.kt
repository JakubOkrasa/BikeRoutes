package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RoutesDatabase

val localDbModule = module {
    single { createDatabase(androidContext()) }
    single { get<RoutesDatabase>().routeDao() }
}

private fun createDatabase(context: Context) =
    Room.databaseBuilder(
        context,
        RoutesDatabase::class.java,
        "route_db"
    ).build()
