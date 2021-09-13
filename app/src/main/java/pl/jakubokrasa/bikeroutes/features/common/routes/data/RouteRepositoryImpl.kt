package pl.jakubokrasa.bikeroutes.features.common.routes.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.common.routes.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class RouteRepositoryImpl(
    private val firestore: FirebaseFirestore,
): RouteRepository {

    override suspend fun addRoute(route: Route, points: List<Point>) {

        val routeResponse = RouteResponse(route)
        val routeDoc = firestore.collection("routes").document()
        val pointsMap = mapOf("pointsArray" to points.map { PointResponse(it) })
        val pointsDoc = firestore.collection("points").document(routeDoc.id)

        firestore.runBatch { batch ->
            //save RouteResponse
            batch.set(routeDoc, routeResponse)

            //save Points
            batch.set(pointsDoc, pointsMap)

            //assign route document id to routeId in the document (which is used in the presentation layer)
            batch.update(routeDoc, "routeId", routeDoc.id)
        }.await()

    }

    override suspend fun getMyRoutes(uid: String): List<Route> {
        val routeResponseList = ArrayList<RouteResponse>()
        val documents =
            firestore
                .collection("routes")
                .whereEqualTo("userId", uid)
                .get().await().documents

        for (doc in documents)
            doc.toObject(RouteResponse::class.java)?.let { routeResponseList.add(it) }
        return routeResponseList.map { it.toRoute()}
    }

    override suspend fun updateRoute(route: Route) {
        val routeResponse = RouteResponse(route)
        firestore
            .collection("routes")
            .document(route.routeId)
            .set(routeResponse)
            .await()
    }

    override suspend fun deleteRoute(route: Route) {
        val routeDoc = firestore.collection("routes").document(route.routeId)
        val pointsDoc = firestore.collection("points").document(route.routeId)

        firestore.runBatch { batch ->
            batch.delete(routeDoc)
            batch.delete(pointsDoc)
        }.await()
    }

    override suspend fun getSharedRoutes(uid: String): List<Route> {
        val routeResponseList = ArrayList<RouteResponse>()
        val documents =
            firestore
                .collection("routes")
                .whereNotEqualTo("userId", uid)
                .whereIn("sharingType", listOf(SharingType.PUBLIC.name, SharingType.PUBLIC_WITH_PRIVATE_PHOTOS.name))
                .get().await().documents

        for (doc in documents)
            doc.toObject(RouteResponse::class.java)?.let { routeResponseList.add(it) }
        return routeResponseList.map { it.toRoute()}
    }
}