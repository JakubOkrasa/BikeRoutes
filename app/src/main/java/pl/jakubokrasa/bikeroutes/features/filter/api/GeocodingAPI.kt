package pl.jakubokrasa.bikeroutes.features.filter.api

import pl.jakubokrasa.bikeroutes.features.filter.api.model.GeocodingItemResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingAPI {

    @GET("search/?format=json&limit=1")
    suspend fun getGeocodingItem(@Query(value = "q", encoded = false) query: String): List<GeocodingItemResponse>
}