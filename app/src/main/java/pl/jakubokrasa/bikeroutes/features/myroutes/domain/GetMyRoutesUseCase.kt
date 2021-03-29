package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.model.Route
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteRepository

class GetMyRoutesUseCase(private val routeRepository: RouteRepository) {

    fun getMyRoutes(): LiveData<List<Route>> {
        Log.d("LOG", "usecase called")
        return routeRepository.getMyRoutes()
    }
}