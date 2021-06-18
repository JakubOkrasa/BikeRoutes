package pl.jakubokrasa.bikeroutes.features.common.data

import pl.jakubokrasa.bikeroutes.features.common.data.model.GeocodingItemResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingAPI {

    @GET("search/?format=json&limit=1")
    suspend fun getGeocodingItem(@Query(value = "q", encoded = false) query: String): GeocodingItemResponse
}