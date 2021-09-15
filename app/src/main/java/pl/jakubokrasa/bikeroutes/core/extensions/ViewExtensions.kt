package pl.jakubokrasa.bikeroutes.core.extensions

import android.view.View
import com.google.android.material.slider.RangeSlider


fun View.makeVisible() = run { visibility = View.VISIBLE }
fun View.makeGone() = run { visibility = View.GONE }
fun View.isVisible() = visibility == View.VISIBLE

fun RangeSlider.getValFrom() = values[0].toInt()
fun RangeSlider.getValTo() = values[1].toInt()
