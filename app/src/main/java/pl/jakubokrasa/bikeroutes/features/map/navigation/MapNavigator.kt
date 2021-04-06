package pl.jakubokrasa.bikeroutes.features.map.navigation

import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator

interface MapNavigator {
    fun openSaveRouteFragment()

    fun goBack()
}