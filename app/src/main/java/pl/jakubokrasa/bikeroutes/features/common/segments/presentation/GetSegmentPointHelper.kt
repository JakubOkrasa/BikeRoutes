package pl.jakubokrasa.bikeroutes.features.common.segments.presentation

import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.presentation.Helper
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point

class GetSegmentPointHelper: Helper<Int, GetSegmentBeginData>() {
    override suspend fun action(params: GetSegmentBeginData): Int {
        var minDistance = MINIMUM_DISTANCE_INITIAL_VALUE
        var currentDistance = -1.0
        var beginPointIndex = -1
        val thresholdDistance = DISTANCE_COEFFICIENT_A * params.zoomLevel + DISTANCE_COEFFICIENT_B

        with(params) {
            for(i in points.indices) {
                currentDistance = params.geoPoint.distanceToAsDouble(GeoPoint(points[i].geoPointData.latitude, points[i].geoPointData.longitude))
                if(currentDistance<minDistance) {
                    minDistance = currentDistance
                    if (minDistance < thresholdDistance) {
                        beginPointIndex = i
                        break
                    }
                }

            }
        }
        if(beginPointIndex!=-1)
            return beginPointIndex
        else
            throw Exception("Tap outside route")

    }

    companion object {
        const val DISTANCE_COEFFICIENT_A = -20
        const val DISTANCE_COEFFICIENT_B = 360
        const val MINIMUM_DISTANCE_INITIAL_VALUE = DISTANCE_COEFFICIENT_B.toDouble()
    }

}

data class GetSegmentBeginData(
    val geoPoint: GeoPoint,
    val points: List<Point>,
    val zoomLevel: Double
)