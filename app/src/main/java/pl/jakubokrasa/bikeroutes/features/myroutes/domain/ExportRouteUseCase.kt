package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
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

class ExportRouteUseCase(private val context: Context): UseCase<Bitmap, ExportRouteData>() {
    @ExperimentalCoroutinesApi
    override suspend fun action(params: ExportRouteData): Bitmap {
      return getSnapshot(params)
    }


    @ExperimentalCoroutinesApi
    suspend fun getSnapshot(params: ExportRouteData): Bitmap = suspendCancellableCoroutine { continuation ->
        val boundingBox: BoundingBox
        with(params.route.boundingBoxData) {
            boundingBox = BoundingBox(
                latNorth, lonEast, latSouth, lonWest)
        }

        MapSnapshot({ snapshot ->

            if (snapshot.status != MapSnapshot.Status.CANVAS_OK) {
                throw Exception("MapSnapshot status is NOT CANVAS_OK")
            }
            val bitmap = Bitmap.createBitmap(snapshot.bitmap)

            continuation.resume(bitmap) { error -> //todo nie jestem pewny czy to przekaże błąd do runcatching
                error.message?.let {
                    throw Exception(it)
                }
            }
        },
            MapSnapshot.INCLUDE_FLAG_UPTODATE,
            MapTileProviderBasic(context, TileSourceFactory.WIKIMEDIA),
            listOf(params.polyline),
            Projection(params.zoom,
                MAP_WIDTH_PIXELS,
                MAP_HEIGHT_PIXELS,
                boundingBox.centerWithDateLine,
                0F,
                true,
                true,
                0,
                0)).run()
    }

    companion object {
        const val MAP_WIDTH_PIXELS = 900
        const val MAP_HEIGHT_PIXELS = 600
    }
}


data class ExportRouteData(
    val route: Route, val polyline: Polyline, val segments: List<Segment>, val zoom: Double
)