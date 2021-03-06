package pl.jakubokrasa.bikeroutes.features.photos.data

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.features.photos.data.model.PhotoInfoResponse
import pl.jakubokrasa.bikeroutes.features.photos.domain.PhotoRepository
import pl.jakubokrasa.bikeroutes.features.photos.domain.model.PhotoInfo
import java.io.File

class PhotoRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val storageRef: StorageReference
): PhotoRepository {
    override suspend fun getPhotos(routeId: String): List<PhotoInfo> {
        return firestore.collection("photos")
            .whereEqualTo("routeId", routeId)
            .get()
            .await()
            .map { doc -> doc
                    .toObject(PhotoInfoResponse::class.java)
                    .toPhotoInfo()
            }
    }

    override suspend fun addPhoto(routeId: String, localPath: String, sharingType: SharingType) {

        //save image to Firebase Cloud Storage
        val uri = Uri.fromFile(File(localPath)) //encode local path (e.g. no polish characters)
        val photoName = "photo_${System.currentTimeMillis()}"
        val photoRef = storageRef.child("routes/$routeId/photos/${photoName}")
        photoRef.putFile(uri).await()

        //save reference to Firestore
        val urlResult = photoRef.downloadUrl.await()
        val photo = PhotoInfoResponse("", routeId, urlResult.toString(), sharingType, photoName)
        val photoDoc = firestore.collection("photos").document()
        firestore.runBatch { batch ->
            batch.set(photoDoc, photo)
            batch.update(photoDoc, "photoId", photoDoc.id)
            batch.update(photoDoc, "name", photoName)
        }.await()
    }

    override suspend fun removePhoto(photoInfo: PhotoInfo) {

        //remove from Cloud Storage
        val photoResponse = PhotoInfoResponse(photoInfo)
        val photoRef = storageRef.child("routes/${photoResponse.routeId}/photos/${photoResponse.name}")
        photoRef.delete().await()

        //remove from Firestore
        firestore.collection("photos")
            .document(photoResponse.photoId)
            .delete().await()

    }
}