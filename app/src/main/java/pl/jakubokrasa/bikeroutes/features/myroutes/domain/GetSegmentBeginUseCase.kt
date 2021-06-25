package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import android.util.Log
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.map.domain.RemoteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import java.lang.Exception

class GetSegmentBeginUseCase(): UseCase<Int, GetSegmentBeginData>() {
    override suspend fun action(params: GetSegmentBeginData): Int {
        var minDistance: Double = 500.0 //todo 50 jest z sufitu
        var currentDistance: Double = -1.0
        var beginPointIndex = -1
        with(params) {
            for(i in points.indices) {
                currentDistance = params.geoPoint.distanceToAsDouble(points[i].geoPoint)
                if(currentDistance<minDistance) {
                    minDistance = currentDistance
                    if (minDistance < 100.0) {
                        beginPointIndex = i
                        break
                    }
                }

            }
        }
        Log.d("TEST", "beginIndex=$beginPointIndex")
        if(beginPointIndex!=-1)
            return beginPointIndex
        else
            throw Exception("Tap outside route")

    }

}

data class GetSegmentBeginData(
    val geoPoint: GeoPoint,
    val points: List<Point>
)