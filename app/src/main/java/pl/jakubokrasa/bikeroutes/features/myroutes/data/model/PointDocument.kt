package pl.jakubokrasa.bikeroutes.features.myroutes.data.model

import pl.jakubokrasa.bikeroutes.features.map.data.remote.model.PointResponse

data class PointDocument(
    val pointsArray: List<PointResponse>
) {
    constructor(): this(ArrayList<PointResponse>())
}