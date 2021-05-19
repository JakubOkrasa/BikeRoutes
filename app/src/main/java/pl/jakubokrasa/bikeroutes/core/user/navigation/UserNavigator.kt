package pl.jakubokrasa.bikeroutes.core.user.navigation

import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.navigation.FragmentNavigator

class UserNavigator(private val fragmentNavigator: FragmentNavigator) {

    fun signInToMap() {
        fragmentNavigator.navigateTo(R.id.action_signInFragment_to_nav_map)
    }

    fun signUpToMap() {
        fragmentNavigator.navigateTo(R.id.action_signUpFragment_to_nav_map)
    }

    fun signUpToSignIn() {
        fragmentNavigator.navigateTo(R.id.action_signUpFragment_to_signInFragment)
    }

    fun signInToSignUp() {
        fragmentNavigator.navigateTo(R.id.action_signInFragment_to_signUpFragment)
    }

    fun signInToForgotPassword() {
        fragmentNavigator.navigateTo(R.id.action_signInFragment_to_forgotPasswordFragment)
    }

    fun forgotPasswordToSignIn() {
        fragmentNavigator.navigateTo(R.id.action_forgotPasswordFragment_to_signInFragment)
    }

    fun accountToSignIn() {
        fragmentNavigator.navigateTo(R.id.action_nav_account_to_signInFragment)
    }

    fun goBack() {
        fragmentNavigator.goBack()
    }
}