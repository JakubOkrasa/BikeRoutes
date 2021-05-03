package pl.jakubokrasa.bikeroutes.features.map.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.domain.RouteRepository
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route

class RouteRepositoryImpl(private val firestore: FirebaseFirestore): RouteRepository {
    override suspend fun addRoute(route: Route) {
//        firestore.collection("routes").add(RouteResponse())
    }

}