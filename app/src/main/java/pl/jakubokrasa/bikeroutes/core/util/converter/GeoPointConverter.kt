package pl.jakubokrasa.bikeroutes.core.util.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import org.osmdroid.util.GeoPoint


class GeoPointConverter {
    // todo AA robi to w inny sposob: jvmstatic + typetoken
    @TypeConverter
    fun toGeoPoint(data: String?): GeoPoint {
        return Gson().fromJson(data, GeoPoint::class.java)
    }

    @TypeConverter
    fun toJson(geoPoint: GeoPoint?): String {
        return Gson().toJson(geoPoint)
    }
}