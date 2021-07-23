package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.core.content.FileProvider
import org.osmdroid.views.drawing.MapSnapshot
import pl.jakubokrasa.bikeroutes.BuildConfig
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import pl.jakubokrasa.bikeroutes.core.util.getFormattedDate
import pl.jakubokrasa.bikeroutes.core.util.getFormattedDistance
import pl.jakubokrasa.bikeroutes.core.util.getFormattedRideTime
import pl.jakubokrasa.bikeroutes.features.map.domain.model.Route
import java.io.File
import java.io.FileOutputStream

class CompleteExportRouteUseCase(private val context: Context): UseCase<Uri, CompleteExportRouteData>() {
    override suspend fun action(params: CompleteExportRouteData): Uri {

        val infoBitmap = loadBitmapFromView(View.inflate(context, R.layout.export_route_info,  null), params.route)
        val mapBitmap = Bitmap.createBitmap(params.mapSnapshot.bitmap)
        return saveImageToLocalCache(mergeExportRouteBitmaps(mapBitmap, infoBitmap), getFileName())

    }

    private fun getUriToExportedMap(snapshot: MapSnapshot): Uri {
        val bitmap = Bitmap.createBitmap(snapshot.bitmap) //todo to powinno być w wątku Default
        return saveImageToLocalCache(bitmap, getFileName())
    }


    private fun saveImageToLocalCache(bitmap: Bitmap, fileName: String): Uri {
        val uri: Uri
        //todo czy tu potrzebne try catch, bo do runCatching błąd trafi?

        val imagesFolder = File(context.cacheDir, "images")
        imagesFolder.mkdirs()
        val file = File(imagesFolder, "$fileName.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        stream.flush()
        stream.close()
        uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID +".fileprovider", file)

        return uri
    }

    private fun getFileName() = "shared_route_${System.currentTimeMillis()}"


    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }

    private fun loadBitmapFromView(view: View, route: Route): Bitmap {
        bindInfoView(view, route)

        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        val bitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, ExportRouteUseCase.MAP_WIDTH_PIXELS, view.measuredHeight)
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
//        tvAvgSpeed.text = getFormattedAvgSpeed(route.avgSpeedKmPerH) todo
        tvRideTime.text = getFormattedRideTime(route.rideTimeMinutes)
    }
}

data class CompleteExportRouteData(
    val mapSnapshot: MapSnapshot,
    val route: Route
)