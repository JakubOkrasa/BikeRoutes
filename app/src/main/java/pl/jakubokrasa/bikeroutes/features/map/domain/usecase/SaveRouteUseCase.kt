package pl.jakubokrasa.bikeroutes.features.map.domain.usecase

import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.user.domain.UserAuth
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.filter.domain.model.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.PointLocalRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class SaveRouteUseCase(
    private val localRepository: PointLocalRepository,
    private val routeRepository: RouteRepository,
    private val userAuth: UserAuth
): UseCase<Unit, DataSaveRoute>() {
    private var rideTimeMinutes = 0

    override suspend fun action(params: DataSaveRoute) {
        val points = localRepository.getPoints2()
        if(points.isNotEmpty()) {
            val route = Route(
                "",
                System.currentTimeMillis(),
                userAuth.getCurrentUserId(),
                params.name,
                params.description,
                params.distance,
                params.sharingType,
                getRideTimeMinutes(points),
                getAvgSpeedKmH(params.distance),
                params.boundingBoxData,
                params.createdBy
            )
            routeRepository.addRoute(route, points)
            localRepository.deletePoints()
        }
    }

    private fun getRideTimeMinutes(points: List<Point>): Int {
        val rideTime = points[points.size - 1].createdAt - points[0].createdAt
        rideTimeMinutes = TimeUnit.MILLISECONDS.toMinutes(rideTime).toInt()
        return rideTimeMinutes
    }

    private fun getAvgSpeedKmH(distanceMeters: Int): Int {
        if(rideTimeMinutes!=0) {
            return ((distanceMeters.toFloat()/1_000.0)/(rideTimeMinutes.toFloat()/60.0)).roundToInt()
        }
        return 0
    }

}

data class DataSaveRoute (
    val name: String,
    val description: String,
    val distance: Int,
    val sharingType: SharingType,
    val boundingBoxData: BoundingBoxData,
    val createdBy: String
)