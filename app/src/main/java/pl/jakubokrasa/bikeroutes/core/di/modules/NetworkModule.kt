package pl.jakubokrasa.bikeroutes.core.di.modules

import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(context.getString(R.string.URL_NOMINATIM_GEOCODING_API))
            .addConverterFactory(GsonConverterFactory.create())
//            .client(get<OkHttpClient>())
            .build()
    }

}