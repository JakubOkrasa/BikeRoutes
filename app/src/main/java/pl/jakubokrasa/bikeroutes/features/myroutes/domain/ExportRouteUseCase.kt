package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.Projection
import org.osmdroid.views.drawing.MapSnapshot
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import java.lang.Exception

class ExportRouteUseCase(private val context: Context): UseCase<MapSnapshot, ExportRouteData>() {
    @ExperimentalCoroutinesApi
    override suspend fun action(params: ExportRouteData): MapSnapshot {

      return getSnapshot(params)
    }


    @ExperimentalCoroutinesApi
    suspend fun getSnapshot(params: ExportRouteData): MapSnapshot = suspendCancellableCoroutine { continuation ->
        val boundingBox: BoundingBox
        with(params.route.boundingBoxData) {
            boundingBox = BoundingBox( //todo możliwe że tu trzeba zamienić parametry miejscami
                latNorth, lonEast, latSouth, lonWest)
        }


        val mapSnapshot = MapSnapshot(
            { snapshot ->
                continuation.resume(
                    snapshot,
                    throw Exception("MapSnapshot error"))
            },
            MapSnapshot.INCLUDE_FLAG_UPTODATE,
            MapTileProviderBasic(context, TileSourceFactory.WIKIMEDIA),
            listOf(params.polyline),
            Projection(MAP_ZOOM,
                MAP_SIZE_PIXELS,
                MAP_SIZE_PIXELS,
                boundingBox.centerWithDateLine,
                0F,
                true,
                true,
                0,
                0))

    }

    companion object {
        const val MAP_SIZE_PIXELS = 300
        const val MAP_ZOOM = 13.0
    }
}


data class ExportRouteData(
    val route: Route, val polyline: Polyline, val segments: List<Segment>
)