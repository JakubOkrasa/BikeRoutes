package pl.jakubokrasa.bikeroutes.core.util

import androidx.room.TypeConverter
import pl.jakubokrasa.bikeroutes.core.user.sharingType

class EnumConverter {

    @TypeConverter
    fun toSharingType(value: Int) = enumValues<sharingType>()[value]

    @TypeConverter
    fun fromSharingType(value: sharingType) = value.ordinal
}