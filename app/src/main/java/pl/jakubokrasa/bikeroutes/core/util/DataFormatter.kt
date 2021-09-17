package pl.jakubokrasa.bikeroutes.core.util

import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.ui_features.myroutes.presentation.MyRoutesFragment.Companion.DISTANCE_SLIDER_VALUE_TO
import java.text.SimpleDateFormat
import java.util.*

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
    val firstCommaIndex = displayName.indexOfComma()
    if(firstCommaIndex != -1) {
        val secondCommaIndex = displayName.indexOfComma(firstCommaIndex + 1)
        if(secondCommaIndex != -1)
            return displayName.substring(0, secondCommaIndex)
        else
            return displayName
    } else {
        return displayName
    }
}

fun getFormattedDate(millis: Long): String {
    val date = Date(millis)
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
    return formatter.format(date)
}
fun getFormattedAvgSpeed(speedKmH: Int) =
    String.format("$speedKmH km/h")

fun getFormattedSharingTypeName(sharingType: SharingType) =
    when(sharingType) {
        SharingType.PUBLIC -> "public"
        SharingType.PRIVATE -> "only me"
        SharingType.PUBLIC_WITH_PRIVATE_PHOTOS -> "private photos"
    }

private fun String.indexOfComma(startIndex: Int = 0): Int {
    return this.indexOf(',', startIndex)
}