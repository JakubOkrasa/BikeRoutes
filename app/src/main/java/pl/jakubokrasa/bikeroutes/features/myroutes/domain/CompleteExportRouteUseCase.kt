package pl.jakubokrasa.bikeroutes.features.myroutes.domain

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import org.osmdroid.views.drawing.MapSnapshot
import pl.jakubokrasa.bikeroutes.core.base.domain.UseCase
import java.io.File
import java.io.FileOutputStream

class CompleteExportRouteUseCase(private val context: Context): UseCase<Uri, MapSnapshot>() {
    override suspend fun action(params: MapSnapshot): Uri {


        return getUriToExportedMap(params)
    }

    private fun getUriToExportedMap(snapshot: MapSnapshot): Uri {
        val bitmap = Bitmap.createBitmap(snapshot.bitmap) //todo to powinno być w wątku Default
        return saveImageToLocalCache(bitmap, getFileName())
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

    private fun getFileName() = "shared_route_${System.currentTimeMillis()}"


    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == state
    }
}