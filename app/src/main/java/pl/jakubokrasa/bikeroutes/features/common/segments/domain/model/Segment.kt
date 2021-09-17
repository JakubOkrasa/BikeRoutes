package pl.jakubokrasa.bikeroutes.features.common.segments.domain.model

data class Segment(
    val segmentId: String,
    val routeId: String,
    val beginIndex: Int,
    val endIndex: Int,
    val segmentType: SegmentType,
    val info: String,
    val segmentColor: String
)
