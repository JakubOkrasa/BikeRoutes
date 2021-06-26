package pl.jakubokrasa.bikeroutes.features.common.segments.data.model

import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.SegmentType
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.SegmentLocationData

data class SegmentResponse(
    val segmentId: String,
    val routeId: String,
    val segmentLocationData: SegmentLocationData,
    val segmentType: SegmentType,
    val info: String
) {
    constructor(): this("", "", SegmentLocationData(0.0, 0.0, 0.0, 0.0), SegmentType.SAND, "")

    constructor(segment: Segment): this(
        segmentId = segment.segmentId,
        routeId = segment.routeId,
        segmentLocationData = segment.segmentLocationData,
        segmentType = segment.segmentType,
        info = segment.info
    )

    fun toSegment() = Segment(
        segmentId = this.segmentId,
        routeId = this.routeId,
        segmentLocationData = this.segmentLocationData,
        segmentType = this.segmentType,
        info = this.info
    )
}
