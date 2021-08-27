package pl.jakubokrasa.bikeroutes.features.common.routes.navigation

import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator
import pl.jakubokrasa.bikeroutes.features.common.photos.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.common.photos.presentation.PhotoGalleryFragment
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model.SegmentDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment

class CommonRoutesNavigator(private val fragmentNavigator: FragmentNavigator) {

    fun openFollowRouteFragment(route: RouteDisplayable, points: List<PointDisplayable>) {
        fragmentNavigator.navigateTo(
            R.id.action_RouteDetailsFragment_to_followRouteFragment,
        null,
            RouteDetailsFragment.ROUTE_BUNDLE_KEY to route,
            RouteDetailsFragment.POINTS_BUNDLE_KEY to points,
        )
    }

    fun openGalleryFragment(photos: List<PhotoInfoDisplayable>, position: Int) {
        fragmentNavigator.navigateTo(
            R.id.action_routeDetailsFragment_to_photoGalleryFragment,
            null,
            PhotoGalleryFragment.PHOTOS_BUNDLE_KEY to photos,
            PhotoGalleryFragment.PHOTO_POSITION_BUNDLE_KEY to position
        )
    }

	fun openSegmentsFragment(route: RouteDisplayable, points: List<PointDisplayable>, segments: List<SegmentDisplayable>) {
        fragmentNavigator.navigateTo(
            R.id.action_routeDetailsFragment_to_segmentsFragment,
            null,
            RouteDetailsFragment.ROUTE_BUNDLE_KEY to route,
            RouteDetailsFragment.POINTS_BUNDLE_KEY to points,
            RouteDetailsFragment.SEGMENTS_BUNDLE_KEY to segments
        )
    }
}