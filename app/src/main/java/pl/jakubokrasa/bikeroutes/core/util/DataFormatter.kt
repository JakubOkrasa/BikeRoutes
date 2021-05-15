package pl.jakubokrasa.bikeroutes.core.util

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