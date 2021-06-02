package pl.jakubokrasa.bikeroutes.core.navigation

import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import pl.jakubokrasa.bikeroutes.core.provider.ActivityProvider

class FragmentNavigatorImpl(
    private val activityProvider: ActivityProvider,
    @IdRes private val navHostFragmentRes: Int,
    @IdRes private val homeDestinationRes: Int,
    private val defaultNavOptions: NavOptions
)
    : FragmentNavigator {

    private fun getFragmentManager() =
        (activityProvider.foregroundActivity as? FragmentActivity)?.supportFragmentManager

    private fun getNavController() =
        getFragmentManager()
            ?.findFragmentById(navHostFragmentRes)
            ?.findNavController()

    override fun navigateTo(destinationId: Int, fragmentTransition: FragmentTransition?) {
        getNavController()?.navigate(destinationId)
    }

    override fun <T> navigateTo(
        destinationId: Int, param: Pair<String, T>?, fragmentTransition: FragmentTransition?
    ) {
        val bundle = param?.let { bundleOf(it) }
        val navOptions = fragmentTransition?.let {
            navOptions {
                anim { enter = it.enterAnim }
                anim { exit = it.exitAnim }
                anim { popEnter = it.popEnterAnim }
                anim { popExit = it.popExitAnim }
            }
        } ?: defaultNavOptions

        getNavController()?.navigate(destinationId, bundle, navOptions)
    }

    override fun <T> navigateTo(
        destinationId: Int, fragmentTransition: FragmentTransition?, vararg params: Pair<String, T>
    ) {
        val bundle = bundleOf(*params)
        val navOptions = fragmentTransition?.let {
            navOptions {
                anim { enter = it.enterAnim }
                anim { exit = it.exitAnim }
                anim { popEnter = it.popEnterAnim }
                anim { popExit = it.popExitAnim }
            }
        } ?: defaultNavOptions

        getNavController()?.navigate(destinationId, bundle, navOptions)
    }

    override fun goBack(destinationId: Int?, inclusive: Boolean) {
        if(destinationId == null) getNavController()?.popBackStack()
        else getNavController()?.popBackStack(destinationId, inclusive)
    }

    override fun clearHistory() {
        goBack(homeDestinationRes)
    }
}












