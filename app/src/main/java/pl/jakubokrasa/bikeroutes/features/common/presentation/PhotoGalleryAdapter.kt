package pl.jakubokrasa.bikeroutes.features.common.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extensions.isVisible
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.databinding.PhotoGalleryItemBinding
import pl.jakubokrasa.bikeroutes.databinding.RvPhotoItemBinding
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.DialogConfirm

class PhotoGalleryAdapter(
    private var context: Context,
    private var photos: List<PhotoInfoDisplayable>,
    ): RecyclerView.Adapter<PhotoGalleryAdapter.PhotoGalleryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PhotoGalleryAdapter.PhotoGalleryViewHolder {
        val binding = PhotoGalleryItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoGalleryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PhotoGalleryAdapter.PhotoGalleryViewHolder, position: Int
    ) {
        val photo = photos[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    override fun onViewAttachedToWindow(holder: PhotoGalleryViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.hideTopBar()
    }


    inner class PhotoGalleryViewHolder(private val binding: PhotoGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoInfoDisplayable) {
            with(binding) {
                topBar.makeGone()
                Glide.with(context)
                    .load(photo.reference)
                    .placeholder(R.drawable.ic_baseline_photo_24)
                    .apply(RequestOptions().centerInside())
                    .into(galleryImage)

                galleryImage.setOnClickListener {
                    if(topBar.isVisible())
                        topBar.makeGone()
                    else
                        topBar.makeVisible()
                }

                ibRemove.setOnClickListener {
//                    DialogConfirm(context, "Are you sure to remove this photo?", "remove", )
                }
            }
        }

        fun hideTopBar() {
            binding.topBar.makeGone()
        }
    }

}