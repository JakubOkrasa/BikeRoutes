package pl.jakubokrasa.bikeroutes.core.extentions

import androidx.fragment.app.Fragment

fun Fragment.replaceChildFragment(fragment: Fragment, frameId: Int) {

    val transaction = childFragmentManager.beginTransaction()
    transaction.replace(frameId, fragment).commit()
}