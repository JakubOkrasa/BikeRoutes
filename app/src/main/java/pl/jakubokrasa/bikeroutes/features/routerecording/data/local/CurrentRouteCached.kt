package pl.jakubokrasa.bikeroutes.features.routerecording.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.osmdroid.util.GeoPoint

@Entity
data class CurrentRouteCached(
    @PrimaryKey(autoGenerate = true)
    private val id: Long, // not sure if it is number
    private var point: GeoPoint
    )