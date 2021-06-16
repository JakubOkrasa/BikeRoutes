package pl.jakubokrasa.bikeroutes.core.util.converter

import androidx.room.TypeConverter
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType

class EnumConverter {

    @TypeConverter
    fun toSharingType(value: Int) = enumValues<SharingType>()[value]

    @TypeConverter
    fun fromSharingType(value: SharingType) = value.ordinal
}