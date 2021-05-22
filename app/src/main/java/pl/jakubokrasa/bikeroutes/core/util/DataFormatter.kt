package pl.jakubokrasa.bikeroutes.core.util

import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO

fun getFormattedRideTime(rideTimeMinutes: Int): String {
    val rideTimeRemainedMinutes = rideTimeMinutes%60
    val rideTimeHours = (rideTimeMinutes - rideTimeRemainedMinutes) / 60
    if(rideTimeHours>0)
        return String.format("%dh %dmin", rideTimeHours, rideTimeRemainedMinutes)
    else {
        if(rideTimeMinutes>0)
            return String.format("%dmin", rideTimeRemainedMinutes)
        else
            return String.format("1min", rideTimeRemainedMinutes)

    }
}

fun getFormattedDistance(distanceMeters: Int): String {
    if (distanceMeters < 1_000)
        return String.format("%dm", distanceMeters)
    else
        return String.format("%.1fkm", (distanceMeters / 1_000.0).toFloat())
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