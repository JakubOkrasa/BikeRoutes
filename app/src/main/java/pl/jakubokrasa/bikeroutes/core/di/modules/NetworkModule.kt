package pl.jakubokrasa.bikeroutes.core.di.modules

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.features.filter.api.GeocodingAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {

    single {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.URL_NOMINATIM_GEOCODING_API))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(GeocodingAPI::class.java) }
}