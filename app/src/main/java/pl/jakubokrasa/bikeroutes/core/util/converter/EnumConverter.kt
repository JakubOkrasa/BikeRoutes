package pl.jakubokrasa.bikeroutes.core.util.converter

import androidx.room.TypeConverter
import pl.jakubokrasa.bikeroutes.core.util.enums.sharingType

class EnumConverter {

    @TypeConverter
    fun toSharingType(value: Int) = enumValues<sharingType>()[value]

    @TypeConverter
    fun fromSharingType(value: sharingType) = value.ordinal
}