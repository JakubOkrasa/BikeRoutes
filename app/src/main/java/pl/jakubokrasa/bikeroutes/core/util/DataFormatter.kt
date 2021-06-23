package pl.jakubokrasa.bikeroutes.core.util

import androidx.room.util.StringUtil
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO

fun getFormattedRideTime(rideTimeMinutes: Int): String {
    val rideTimeRemainedMinutes = rideTimeMinutes%60
    val rideTimeHours = (rideTimeMinutes - rideTimeRemainedMinutes) / 60
    if(rideTimeHours>0)
        return String.format("%d h %d min", rideTimeHours, rideTimeRemainedMinutes)
    else {
        if(rideTimeMinutes>0)
            return String.format("%d min", rideTimeRemainedMinutes)
        else
            return String.format("1 min", rideTimeRemainedMinutes)

    }
}

fun getFormattedDistance(distanceMeters: Int): String {
    if (distanceMeters < 1_000)
        return String.format("%d m", distanceMeters)
    else
        return String.format("%.1f km", (distanceMeters / 1_000.0).toFloat())
}

fun getFormattedFilterDistance(valueFrom: Int, valueTo: Int): String {
    if(valueTo<DISTANCE_SLIDER_VALUE_TO.toInt())
        return String.format("%d km - %d km", valueFrom, valueTo)
    else
        return String.format("%d km - %d+ km", valueFrom, DISTANCE_SLIDER_VALUE_TO.toInt())
}

fun getFormattedFilterDistanceGreaterThan(valueFrom: Int): String {
    return String.format("> %d km", valueFrom)
}

fun getFormattedFilterDistanceLessThan(valueTo: Int): String {
    return String.format("< %d km", valueTo)
}

fun getFormattedFilterLocation(displayName: String): String {
    val secondPartEnd = displayName.indexOf(',', displayName.indexOf(',')+1)
    return displayName.substring(0, secondPartEnd)
}