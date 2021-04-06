package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import androidx.core.content.edit
import pl.jakubokrasa.bikeroutes.core.base.UseCase
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteRepository
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper.Companion.PREF_KEY_DISTANCE_SUM
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper.Companion.PREF_KEY_LAST_LAT
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceHelper.Companion.PREF_KEY_LAST_LNG

class PutRouteSaveDataUseCase(private val routeRepository: RouteRepository, private val preferenceHelper: PreferenceHelper): UseCase<Unit, DataRouteSave>() {
    override suspend fun action(params: DataRouteSave) {
        val id = routeRepository.getCurrentRouteId()
        routeRepository.updateRouteDescription(id, params.description)
        routeRepository.updateRouteDistance(id, params.distance)
        routeRepository.updateRouteName(id, params.name) // causes error
        preparePrefsForNextRoute()
    }

    private fun preparePrefsForNextRoute() {
        with(preferenceHelper.preferences) {
            edit {
                remove(PREF_KEY_LAST_LAT)
                remove(PREF_KEY_LAST_LNG)
                remove(PREF_KEY_DISTANCE_SUM)
            }
        }
    }
}

data class DataRouteSave (
    val name: String,
    val description: String,
    val distance: Int
    )