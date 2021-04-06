package pl.jakubokrasa.bikeroutes.features.map.navigation

import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator

class MapNavigatorImpl(private val fragmentNavigator: FragmentNavigator): MapNavigator {
    override fun openSaveRouteFragment() {
        fragmentNavigator.navigateTo(R.id.action_map_to_save_route)
    }

    override fun goBack() {
        fragmentNavigator.goBack()
    }
}