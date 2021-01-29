package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.CurrentRoutePointDatabase

val localDbModule = module {
    single { buildDb(androidContext()) }
    single { get<CurrentRoutePointDatabase>().currentRoutePointDao() }
}

private fun buildDb(context: Context) =
    Room.databaseBuilder(
        context,
        CurrentRoutePointDatabase::class.java,
        "currentroute_db"
    ).build()
