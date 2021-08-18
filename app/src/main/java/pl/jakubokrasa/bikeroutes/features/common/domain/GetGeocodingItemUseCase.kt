package pl.jakubokrasa.bikeroutes.features.common.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.data.GeocodingAPI
import pl.jakubokrasa.bikeroutes.features.common.domain.model.GeocodingItem
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository

class GetGeocodingItemUseCase(private val api: GeocodingAPI): UseCase<GeocodingItem, String>() {
    override suspend fun action(params: String): GeocodingItem {
        return api.getGeocodingItem(params)[0].toGeocodingItem()
    }
}