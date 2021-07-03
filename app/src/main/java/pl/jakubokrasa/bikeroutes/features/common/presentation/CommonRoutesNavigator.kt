package pl.jakubokrasa.bikeroutes.features.common.presentation

import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment

class CommonRoutesNavigator(private val fragmentNavigator: FragmentNavigator) {

    fun openFollowRouteFragment(route: RouteDisplayable, points: List<PointDisplayable>) {
        fragmentNavigator.navigateTo(
            R.id.action_RouteDetailsFragment_to_followRouteFragment,
        null,
            RouteDetailsFragment.ROUTE_TO_FOLLOW_KEY to route,
            RouteDetailsFragment.POINTS_TO_FOLLOW_KEY to points,
        )
    }

    fun openGalleryFragment(photos: List<PhotoInfoDisplayable>, position: Int) {
        fragmentNavigator.navigateTo(
            R.id.action_followRouteFragment_to_photoGalleryFragment,
            null,
            PhotoGalleryFragment.PHOTOS_BUNDLE_KEY to photos,
            PhotoGalleryFragment.PHOTO_POSITION_BUNDLE_KEY to position
        )
    }
}