package pl.jakubokrasa.bikeroutes.features.common.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.jakubokrasa.bikeroutes.databinding.RvPhotoItemBinding
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable

class PhotosRecyclerAdapter: RecyclerView.Adapter<PhotosRecyclerAdapter.PhotosViewHolder>() {

    var onItemClick: ((PhotoInfoDisplayable) -> Unit)? = null
    private var photos = mutableListOf<PhotoInfoDisplayable>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding = RvPhotoItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosViewHolder(binding)
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

    inner class PhotosViewHolder(private val binding: RvPhotoItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoInfoDisplayable) {
            with(binding) {
                tvTest.text = photo.reference.substringAfterLast('/')
            }
        }
        init {
            // Define click listener for the MyRoutesViewHolder's View.
            itemView.setOnClickListener {
                onItemClick?.invoke(photos[adapterPosition])
            }
        }
    }
}