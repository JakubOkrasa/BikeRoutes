package pl.jakubokrasa.bikeroutes.features.segments.data.model

import pl.jakubokrasa.bikeroutes.features.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.segments.domain.model.SegmentType

data class SegmentResponse(
    val segmentId: String,
    val routeId: String,
    val beginIndex: Int,
    val endIndex: Int,
    val segmentType: SegmentType,
    val info: String,
    val segmentColor: String
) {
    constructor(): this("", "", 0, 0, SegmentType.SAND, "", "")

    constructor(segment: Segment): this(
        segmentId = segment.segmentId,
        routeId = segment.routeId,
        beginIndex = segment.beginIndex,
        endIndex = segment.endIndex,
        segmentType = segment.segmentType,
        info = segment.info,
        segmentColor = segment.segmentColor
    )

    fun toSegment() = Segment(
        segmentId = this.segmentId,
        routeId = this.routeId,
        beginIndex = this.beginIndex,
        endIndex = this.endIndex,
        segmentType = this.segmentType,
        info = this.info,
        segmentColor = this.segmentColor
    )
}
