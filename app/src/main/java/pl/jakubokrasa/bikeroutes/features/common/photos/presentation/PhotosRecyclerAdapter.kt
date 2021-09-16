package pl.jakubokrasa.bikeroutes.features.common.photos.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.RvPhotoItemBinding
import pl.jakubokrasa.bikeroutes.features.common.photos.presentation.model.PhotoInfoDisplayable


class PhotosRecyclerAdapter: RecyclerView.Adapter<PhotosRecyclerAdapter.PhotosViewHolder>() {

    var onItemClick: ((List<PhotoInfoDisplayable>, Int) -> Unit)? = null
    private var photos = mutableListOf<PhotoInfoDisplayable>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = RvPhotoItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val photo = photos[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    fun setItems(photos: List<PhotoInfoDisplayable>) {
        if (photos.isNotEmpty()) this.photos.clear()
        this.photos.addAll(photos)
        notifyDataSetChanged()
    }

    inner class PhotosViewHolder(
        private val binding: RvPhotoItemBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoInfoDisplayable) {
            with(binding) {
                Glide.with(context)
                    .load(photo.downloadUrl)
                    .placeholder(R.drawable.ic_baseline_photo_24)
                    .centerCrop()
                    .into(smallPhoto)
            }
        }
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(photos, adapterPosition)
            }
        }
    }
}