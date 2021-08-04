package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.core.content.FileProvider
import kotlinx.coroutines.*
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.Projection
import org.osmdroid.views.drawing.MapSnapshot
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.BuildConfig
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.util.getFormattedAvgSpeed
import pl.jakubokrasa.bikeroutes.core.util.getFormattedDistance
import pl.jakubokrasa.bikeroutes.core.util.getFormattedRideTime
import pl.jakubokrasa.bikeroutes.core.util.mapTileSource
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import java.io.File
import java.io.FileOutputStream

class ExportRouteHelper(private val context: Context) {
    @ExperimentalCoroutinesApi
    suspend fun action(exportRouteData: ExportRouteData): Uri {
        val snapshot = getSnapshot(exportRouteData)
        return getUriOfExportImage(snapshot, exportRouteData.route)
    }

    @ExperimentalCoroutinesApi
    operator fun invoke(
        exportRouteData: ExportRouteData,
        scope: CoroutineScope,
        onResult: (Result<Uri>) -> Unit = {}
    ) {
        scope.launch {
            val result = withContext(Dispatchers.Main) {
                runCatching { action(exportRouteData) }
            }
        onResult(result)
        }
    }

    private suspend fun getUriOfExportImage(snapshot: MapSnapshot, route: Route) = withContext(
        Dispatchers.Default) {
        val infoBitmap = loadBitmapFromView(View.inflate(context, R.layout.export_route_info,  null), route)
        val mapBitmap = Bitmap.createBitmap(snapshot.bitmap)
        val outBitmap = mergeExportRouteBitmaps(mapBitmap, infoBitmap)
        return@withContext saveImageToLocalCache(outBitmap, getFileName())
    }

    @ExperimentalCoroutinesApi
    suspend fun getSnapshot(params: ExportRouteData): MapSnapshot = suspendCancellableCoroutine { continuation ->
        val boundingBox: BoundingBox
        with(params.route.boundingBoxData) {
            boundingBox = BoundingBox(latNorth, lonEast, latSouth, lonWest)
        }

        MapSnapshot({ snapshot ->
            if (snapshot.status != MapSnapshot.Status.CANVAS_OK) {
                throw Exception("MapSnapshot status is NOT CANVAS_OK")
            }
            continuation.resume(snapshot) { error ->
                error.message?.let {
                    throw Exception(it)
                }
            }
        },
            MapSnapshot.INCLUDE_FLAG_UPTODATE,
            MapTileProviderBasic(context, mapTileSource),
            listOf(params.polyline),
            Projection(params.zoom+1.0,
                MAP_WIDTH_PIXELS,
                MAP_HEIGHT_PIXELS,
                boundingBox.centerWithDateLine,
                0F,
                true,
                true,
                0,
                0)).run()
    }

    private fun saveImageToLocalCache(bitmap: Bitmap, fileName: String): Uri {
        val imagesFolder = File(context.cacheDir, "images")
        imagesFolder.mkdirs()
        val file = File(imagesFolder, "$fileName.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        stream.flush()
        stream.close()
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".fileprovider", file)
    }

    private fun getFileName() = "shared_route_${System.currentTimeMillis()}"

    private fun loadBitmapFromView(view: View, route: Route): Bitmap {
        bindInfoView(view, route)
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, MAP_WIDTH_PIXELS, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }

    private fun mergeExportRouteBitmaps(mapBitmap: Bitmap, infoBitmap: Bitmap): Bitmap {
        val outBitmap = Bitmap.createBitmap(mapBitmap.width, mapBitmap.height+infoBitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(outBitmap)
        canvas.drawBitmap(mapBitmap, 0f, 0f, null)
        canvas.drawBitmap(infoBitmap, 0f, mapBitmap.height.toFloat(), null)
        return outBitmap
    }

    private fun bindInfoView(view: View, route: Route) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvDistance = view.findViewById<TextView>(R.id.tv_distance)
        val tvAvgSpeed = view.findViewById<TextView>(R.id.tv_avg_speed)
        val tvRideTime = view.findViewById<TextView>(R.id.tv_ride_time)

        tvName.text = route.name
        tvDistance.text = getFormattedDistance(route.distance)
        tvAvgSpeed.text = getFormattedAvgSpeed(route.avgSpeedKmPerH)
        tvRideTime.text = getFormattedRideTime(route.rideTimeMinutes)
    }

    companion object {
        const val MAP_WIDTH_PIXELS = 1800
        const val MAP_HEIGHT_PIXELS = 1200
    }
}

data class ExportRouteData(
    val route: Route,
    val polyline: Polyline,
    val zoom: Double
)