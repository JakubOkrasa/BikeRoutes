package pl.jakubokrasa.bikeroutes.features.myroutes.data.model

import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData

data class GeoPointDataResponse(
    val longitude: Double,
    val latitude: Double,
)
{
    constructor(): this(0.0, 0.0) //for firestore

    constructor(geoPoint: GeoPointData): this(
        geoPoint.longitude,
        geoPoint.latitude
    )
}
