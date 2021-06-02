package pl.jakubokrasa.bikeroutes.features.common.domain

data class FilterData(
    val minDistanceKm: Int? = null,
    val maxDistanceKm: Int? = null
)
