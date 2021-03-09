package pl.jakubokrasa.bikeroutes.core.app

import android.app.Application
import android.content.res.Configuration
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.jakubokrasa.bikeroutes.core.di.koinInjector

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

//    private fun startDB() {
////        val db: RouteAndPointDatabase = Room.databaseBuilder(
////            applicationContext,
////            RouteAndPointDatabase::class.java,
////            "route_db"
////        ).build()
////        val dao: RouteAndPointDao = db.routeDao()
////        val repo: RouteRepositoryImpl = RouteRepositoryImpl(dao)
//        val repo: RouteRepositoryImpl by inject()
//        val dao: RouteAndPointDao by inject()
//        runBlocking {
////            val usecase = GetCurrentRouteUseCase(repo)
////            usecase(Unit, GlobalScope)
//            dao.insertRoute(RouteCached(200L, true))
//        }
//    }


    private fun startKoin() {
        startKoin {
            androidContext(this@App)
            modules(koinInjector)
        }
    }
}