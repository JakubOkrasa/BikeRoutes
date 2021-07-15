package pl.jakubokrasa.bikeroutes.features.map.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Point
import java.io.Serializable

data class PointDisplayable(
    val pointId: Long,
    val geoPoint: GeoPoint
): Serializable {
    constructor(point: Point) : this (
        pointId = point.pointId,
        geoPoint = point.geoPoint
    )

    fun toPointNoCreatedAt(): Point {
        return Point(
            this.pointId,
            GeoPoint(this.geoPoint.latitude, this.geoPoint.longitude),
            0,
            false,
            false
        )
    }
}