package pl.jakubokrasa.bikeroutes.features.points.data.remote.model

// for Firestore model deserializer
data class PointDocument(
    val pointsArray: List<PointResponse>
) {
    constructor(): this(ArrayList<PointResponse>())
}