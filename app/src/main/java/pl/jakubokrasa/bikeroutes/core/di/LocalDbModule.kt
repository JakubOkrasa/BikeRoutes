package pl.jakubokrasa.bikeroutes.core.di

import android.content.Context
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.features.map.data.local.PointRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.map.data.local.PointDatabase

val localDbModule = module {
    single { createDatabase(androidContext()) }
    single { get<PointDatabase>().routeDao() }

    single { PointRepositoryImpl(get()) }
}

private fun createDatabase(context: Context) =
    Room.databaseBuilder(
        context,
        PointDatabase::class.java,
        "route_db"
    )
        .fallbackToDestructiveMigration()
        .build()
