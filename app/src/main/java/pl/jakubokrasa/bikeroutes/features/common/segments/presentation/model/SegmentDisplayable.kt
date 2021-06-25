package pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model

import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.SegmentType
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.SegmentLocationData

data class SegmentDisplayable(
    val segmentId: String,
    val routeId: String,
    val segmentLocationData: SegmentLocationData,
    val segmentType: SegmentType,
    val comment: String
) {
    constructor(segment: Segment): this(
        segmentId = segment.segmentId,
        routeId = segment.routeId,
        segmentLocationData = segment.segmentLocationData,
        segmentType = segment.segmentType,
        comment = segment.comment
    )

    fun toSegment() = Segment(
        segmentId = this.segmentId,
        routeId = this.routeId,
        segmentLocationData = this.segmentLocationData,
        segmentType = this.segmentType,
        comment = this.comment
    )
}
