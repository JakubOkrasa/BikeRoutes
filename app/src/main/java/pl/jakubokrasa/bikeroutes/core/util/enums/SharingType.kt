package pl.jakubokrasa.bikeroutes.core.util.enums

import java.util.*

enum class SharingType {
    PRIVATE,
    PUBLIC,
    PUBLIC_WITH_PRIVATE_PHOTOS;

    override fun toString() = this.name.toLowerCase(Locale.ROOT).replace('_', ' ')
}