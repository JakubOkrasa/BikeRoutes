package pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model

import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.SegmentType

data class SegmentDisplayable(
    val segmentId: String,
    val routeId: String,
    val beginIndex: Int,
    val endIndex: Int,
    val segmentType: SegmentType,
    val info: String,
    val segmentColor: String
) {
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
