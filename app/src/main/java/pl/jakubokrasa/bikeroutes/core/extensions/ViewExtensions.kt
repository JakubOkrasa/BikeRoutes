package pl.jakubokrasa.bikeroutes.core.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.google.android.material.slider.RangeSlider

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)

fun View.makeVisible() = run { visibility = View.VISIBLE }

fun View.makeGone() = run { visibility = View.GONE }

fun RangeSlider.getValFrom(): Int {
    return values[0].toInt()
}

fun RangeSlider.getValTo(): Int {
    return values[1].toInt()
}