package pl.jakubokrasa.bikeroutes.features.map.navigation

import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator
import pl.jakubokrasa.bikeroutes.features.common.domain.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.map.presentation.MapFragment.Companion.BOUNDING_BOX_DATA_KEY

class MapFrgNavigator(private val fragmentNavigator: FragmentNavigator) {
    fun openSaveRouteFragment(boundingBoxData: BoundingBoxData) {
        fragmentNavigator.navigateTo(R.id.action_map_to_save_route,
        null,
        BOUNDING_BOX_DATA_KEY to boundingBoxData)
    }

    fun goBack() {
        fragmentNavigator.goBack()
    }
}