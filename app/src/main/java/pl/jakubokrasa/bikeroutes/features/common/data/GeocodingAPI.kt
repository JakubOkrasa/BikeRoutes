package pl.jakubokrasa.bikeroutes.features.common.data

import pl.jakubokrasa.bikeroutes.features.common.data.model.GeocodingItem
import pl.jakubokrasa.bikeroutes.features.common.data.model.GeocodingResponse
import retrofit2.http.GET

interface GeocodingAPI {

    @GET("search/?format=json&q=warsaw")
    suspend fun getLocations(): List<GeocodingItem>
}