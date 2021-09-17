package pl.jakubokrasa.bikeroutes.features.myroutes.navigation


import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator
import pl.jakubokrasa.bikeroutes.features.common.points.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.common.routes.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment

class MyRoutesNavigator(private val fragmentNavigator: FragmentNavigator) {

    fun openRouteDetailsFragment(route: RouteDisplayable, points: List<PointDisplayable>) {
        fragmentNavigator.navigateTo(
            R.id.action_my_routes_to_routeDetailsFragment,
            null,
        RouteDetailsFragment.ROUTE_BUNDLE_KEY to route,
                RouteDetailsFragment.POINTS_BUNDLE_KEY to points,
                RouteDetailsFragment.IS_MY_ROUTE_BUNDLE_KEY to true)
    }

    fun goBack() {
        fragmentNavigator.goBack()
    }
}