package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import android.util.Log
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import java.lang.Exception

class GetSegmentPointUseCase(): UseCase<Int, GetSegmentBeginData>() {
    override suspend fun action(params: GetSegmentBeginData): Int {
        var minDistance = MINIMUM_DISTANCE_INITIAL_VALUE
        var currentDistance = -1.0
        var beginPointIndex = -1
        val thresholdDistance = DISTANCE_COEFFICIENT_A * params.zoomLevel + DISTANCE_COEFFICIENT_B

        with(params) {
            for(i in points.indices) {
                currentDistance = params.geoPoint.distanceToAsDouble(points[i].geoPoint)
                if(currentDistance<minDistance) {
                    minDistance = currentDistance
                    if (minDistance < thresholdDistance) { //todo tu może być jeszcze sprawdzanie czy następny jest bliżej, jeśli tak sprawdź jeszcze następny, jeśli nie - weź ten
                                                            //todo można by jeszcze pozwolić na wybór punktu pomiędzy punktami z route
                                                            //todo wtedy do SegmentLocationData byłyby po dwie lokalizacje dla punktu (więc w sumie 4)
                                                            //todo pierwsze to dokładny zaznaczony punkt, od niego szła by polyline w linii prostej do drugiego - należącego do route
//                        while(params.geoPoint.distanceToAsDouble(points[i+1].geoPoint)<minDistance) i++
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
        private const val MAX_ZOOM_LEVEL = 16.0
        const val MINIMUM_DISTANCE_INITIAL_VALUE = DISTANCE_COEFFICIENT_B.toDouble()
    }

}

data class GetSegmentBeginData(
    val geoPoint: GeoPoint,
    val points: List<Point>,
    val zoomLevel: Double
)