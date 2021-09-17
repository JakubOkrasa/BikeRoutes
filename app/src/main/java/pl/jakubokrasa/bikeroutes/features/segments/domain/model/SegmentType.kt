package pl.jakubokrasa.bikeroutes.features.segments.domain.model

import java.util.*

enum class SegmentType() {
    SAND,
    BUMPS,
    HILLY_GROUND,
    TRAFFIC,
    OTHER;

    override fun toString() = this.name.toLowerCase(Locale.ROOT).replace("_", " ")
}