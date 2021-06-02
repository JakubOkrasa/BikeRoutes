package pl.jakubokrasa.bikeroutes.features.sharedroutes.navigation


import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.FollowRouteFragment

class SharedRoutesNavigator(private val fragmentNavigator: FragmentNavigator) {

    fun openFollowRouteFragment(route: RouteDisplayable, points: List<PointDisplayable>) {
        fragmentNavigator.navigateTo(
            R.id.action_shared_routes_to_followRouteFragment,
            null,
            FollowRouteFragment.ROUTE_TO_FOLLOW_KEY to route,
                    FollowRouteFragment.POINTS_TO_FOLLOW_KEY to points,
                    FollowRouteFragment.IS_MY_ROUTE_KEY to false,
            )

    }

    fun goBack() {
        fragmentNavigator.goBack()
    }
}