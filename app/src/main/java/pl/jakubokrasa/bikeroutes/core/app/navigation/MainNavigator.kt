package pl.jakubokrasa.bikeroutes.core.app.navigation

import androidx.annotation.IdRes
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import pl.jakubokrasa.bikeroutes.core.provider.ActivityProvider

class MainNavigator(
    private val activityProvider: ActivityProvider,
    @IdRes private val navHostFragmentRes: Int
    ) {

    private fun getFragmentManager() =
        (activityProvider.foregroundActivity as? FragmentActivity)?.supportFragmentManager

    private fun getNavController() =
        getFragmentManager()
            ?.findFragmentById(navHostFragmentRes)
            ?.findNavController()

    fun navigateTo(destinationId: Int) {
        getNavController()?.navigate(destinationId)
    }
}