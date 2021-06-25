package pl.jakubokrasa.bikeroutes.features.common.segments.domain.model

data class Segment(
    val segmentId: String,
    val routeId: String,
    val segmentLocationData: SegmentLocationData,
    val segmentType: SegmentType,
    val comment: String
)

data class SegmentLocationData(
    val startLat: Double,
    val startLon: Double,
    val endLat: Double,
    val endLon: Double
)
