package pl.jakubokrasa.bikeroutes.features.common.filter.domain

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.filter.api.GeocodingAPI
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.GeocodingItem

class GetGeocodingItemUseCase(private val api: GeocodingAPI): UseCase<GeocodingItem, String>() {
    override suspend fun action(params: String): GeocodingItem {
        return api.getGeocodingItem(params)[0].toGeocodingItem()
    }
}