package pl.jakubokrasa.bikeroutes.features.map.navigation

import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator

class MapFrgNavigator(private val fragmentNavigator: FragmentNavigator) {
    fun openSaveRouteFragment() {
        fragmentNavigator.navigateTo(R.id.action_map_to_save_route)
    }

    fun goBack() {
        fragmentNavigator.goBack()
    }
}