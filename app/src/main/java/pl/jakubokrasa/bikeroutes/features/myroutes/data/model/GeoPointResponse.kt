package pl.jakubokrasa.bikeroutes.features.myroutes.data.model

import org.osmdroid.util.GeoPoint

data class GeoPointResponse(
    val longitude: Double,
    val latitude: Double,
)
{
    constructor(): this(0.0, 0.0) //for firestore

    constructor(geoPoint: GeoPoint): this(
        geoPoint.longitude,
        geoPoint.latitude
    )
}
