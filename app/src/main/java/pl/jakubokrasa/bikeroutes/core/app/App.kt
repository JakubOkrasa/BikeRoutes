package pl.jakubokrasa.bikeroutes.core.app

import android.app.Application
import android.content.res.Configuration
import com.gu.toolargetool.TooLargeTool
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import pl.jakubokrasa.bikeroutes.core.di.koinInjector

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        TooLargeTool.startLogging(this);
        startKoin()
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@App)
            modules(koinInjector)
        }
    }
}