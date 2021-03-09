package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.routerecording.data.RouteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RouteAndPointDatabase

val localDbModule = module {
    single { createDatabase(androidContext()) }
    single { get<RouteAndPointDatabase>().routeDao() }

    single { RouteRepositoryImpl(get()) }
}

private fun createDatabase(context: Context) =
    Room.databaseBuilder(
        context,
        RouteAndPointDatabase::class.java,
        "route_db"
    )
        .fallbackToDestructiveMigration()
        .build()
