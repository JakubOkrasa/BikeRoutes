package pl.jakubokrasa.bikeroutes.core.util.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData


class GeoPointConverter {
    @TypeConverter
    fun toGeoPoint(data: String?): GeoPointData
        = Gson().fromJson(data, GeoPointData::class.java)

    @TypeConverter
    fun toJson(geoPointData: GeoPointData?)
        = Gson().toJson(geoPointData)
}