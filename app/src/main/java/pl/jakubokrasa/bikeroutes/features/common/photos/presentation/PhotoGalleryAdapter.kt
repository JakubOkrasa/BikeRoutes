package pl.jakubokrasa.bikeroutes.features.common.photos.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extensions.isVisible
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.databinding.PhotoGalleryItemBinding
import pl.jakubokrasa.bikeroutes.features.common.photos.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel

class PhotoGalleryAdapter(
    private val context: Context,
    private var photos: ArrayList<PhotoInfoDisplayable>,
    private val viewModel: MyRoutesViewModel
    ): RecyclerView.Adapter<PhotoGalleryAdapter.PhotoGalleryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): PhotoGalleryViewHolder {
        val binding = PhotoGalleryItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotoGalleryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PhotoGalleryViewHolder, position: Int
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

    fun removePhoto(position: Int) {
        photos.removeAt(position)
        if(photos.isEmpty())
            viewModel.navigateBack()
        else
            notifyDataSetChanged()
    }


    inner class PhotoGalleryViewHolder(private val binding: PhotoGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoInfoDisplayable) {
            with(binding) {
                topBar.makeGone()
                Glide.with(context)
                    .load(photo.downloadUrl)
                    .placeholder(R.drawable.ic_baseline_photo_24)
                    .apply(RequestOptions().centerInside())
                    .into(galleryImage)

                galleryImage.setOnClickListener(galleryImageOnClick)

                ibRemovePhoto.setOnClickListener {
                    DialogRemovePhoto(context, viewModel, photo, adapterPosition).show()
                }
            }
        }

        fun hideTopBar() {
            binding.topBar.makeGone()
        }

        private val galleryImageOnClick = View.OnClickListener {
            with(binding) {
                if(topBar.isVisible())
                    topBar.makeGone()
                else
                    topBar.makeVisible()
            }
        }
    }

}