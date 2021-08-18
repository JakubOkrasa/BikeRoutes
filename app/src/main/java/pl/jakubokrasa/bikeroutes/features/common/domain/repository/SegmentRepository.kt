package pl.jakubokrasa.bikeroutes.features.common.domain.repository

import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment

interface SegmentRepository {
    suspend fun getSegments(routeId: String): List<Segment>
    suspend fun addSegment(segment: Segment)
    suspend fun removeSegment(segmentId: String)
}