package pl.jakubokrasa.bikeroutes.features.myroutes.navigation


import android.os.Parcelable
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator
import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.RouteResponse
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.FollowRouteFragment

class MyRoutesNavigator(private val fragmentNavigator: FragmentNavigator) {

    fun openFollowRouteFragment(route: RouteDisplayable, points: List<PointDisplayable>) {

        fragmentNavigator.navigateTo(
            R.id.action_my_routes_to_followRouteFragment,
        FollowRouteFragment.ROUTE_TO_FOLLOW_KEY to route,
            FollowRouteFragment.POINTS_TO_FOLLOW_KEY to points,
        null)

    }

    fun goBack() {
        fragmentNavigator.goBack()
    }
}