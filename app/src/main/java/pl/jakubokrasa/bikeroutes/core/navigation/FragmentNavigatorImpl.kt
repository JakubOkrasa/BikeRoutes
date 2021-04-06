package pl.jakubokrasa.bikeroutes.core.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import pl.jakubokrasa.bikeroutes.core.provider.ActivityProvider

class FragmentNavigatorImpl(
    private val activityProvider: ActivityProvider,
    @IdRes private val navHostFragmentRes: Int,
    @IdRes private val homeDestinationRes: Int)
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
        TODO("Not yet implemented")
    }

    override fun goBack(destinationId: Int?, inclusive: Boolean) {
        if(destinationId == null) getNavController()?.popBackStack()
        else getNavController()?.popBackStack(destinationId, inclusive)
    }

    override fun clearHistory() {
        goBack(homeDestinationRes)
    }
}