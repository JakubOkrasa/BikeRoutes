package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.*
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.views.Projection
import org.osmdroid.views.drawing.MapSnapshot
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.features.common.segments.domain.model.Segment
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import java.io.File
import java.io.FileOutputStream

class ExportRouteUseCase(private val context: Context): UseCase<Uri, ExportRouteData>() {
    @ExperimentalCoroutinesApi
    override suspend fun action(params: ExportRouteData): Uri {
        val snapshot = getSnapshot(params)
        return getUri(snapshot, params.route.routeId)
    }

    private fun getUri(
        snapshot: MapSnapshot, fileName: String
    ): Uri {

        val bitmap = Bitmap.createBitmap(snapshot.bitmap) //todo to powinno być w wątku Default




        return saveImageToLocalCache(bitmap, fileName)
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
            continuation.resume(snapshot) { error -> //todo nie jestem pewny czy to przekaże błąd do runcatching
                error.message?.let {
                    throw Exception(it)
                }
            }
        },
            MapSnapshot.INCLUDE_FLAG_UPTODATE,
            MapTileProviderBasic(context, TileSourceFactory.WIKIMEDIA),
            listOf(params.polyline).plus(params.segmentPolylines),
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

    private fun saveImageToLocalCache(bitmap: Bitmap, fileName: String): Uri {
            val uri: Uri
    //todo czy tu potrzebne try catch, bo do runCatching błąd trafi?

            // local storage only for API<24
            if(!isExternalStorageWritable()) throw Exception("External storage is not writable")
                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "$fileName.png")
                val stream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                stream.close()
                uri = Uri.fromFile(file)

        // file provider, doesn't work
//        val imagesFolder = File(context.cacheDir, "images")
//        imagesFolder.mkdirs()
//        val file = File(imagesFolder, "$fileName.png")
//        val stream = FileOutputStream(file)
//        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
//        stream.close()
//        uri = FileProvider.getUriForFile(context, context.resources.getString(R.string.file_provider_path), file)

            return uri
    }

    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }



    companion object {
        const val MAP_WIDTH_PIXELS = 900
        const val MAP_HEIGHT_PIXELS = 600
    }
}


data class ExportRouteData(
    val route: Route,
    val polyline: Polyline,
    val segmentPolylines: List<Polyline>,
    val zoom: Double
)