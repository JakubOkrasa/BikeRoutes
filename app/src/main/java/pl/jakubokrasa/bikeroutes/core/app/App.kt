package pl.jakubokrasa.bikeroutes.core.app

import android.app.Application
import android.content.res.Configuration
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.di.koinInjector
import pl.jakubokrasa.bikeroutes.features.routerecording.data.RouteRepositoryImpl
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RouteDao
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.RoutesDatabase
import pl.jakubokrasa.bikeroutes.features.routerecording.data.local.model.RouteCached
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.GetCurrentRouteUseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteRepository

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin()
        startDB()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    private fun startDB() {
//        val db: RoutesDatabase = Room.databaseBuilder(
//            applicationContext,
//            RoutesDatabase::class.java,
//            "route_db"
//        ).build()
//        val dao: RouteDao = db.routeDao()
//        val repo: RouteRepositoryImpl = RouteRepositoryImpl(dao)
        val repo: RouteRepositoryImpl by inject()
        val dao: RouteDao by inject()
        runBlocking {
//            val usecase = GetCurrentRouteUseCase(repo)
//            usecase(Unit, GlobalScope)
            dao.insertRoute(RouteCached(200L, true))
        }

    }

    private fun startKoin() {
        startKoin {
            androidContext(this@App)
            modules(koinInjector)
        }
    }
}