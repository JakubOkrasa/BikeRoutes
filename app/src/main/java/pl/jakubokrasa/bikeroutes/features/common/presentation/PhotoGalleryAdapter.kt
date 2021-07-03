package pl.jakubokrasa.bikeroutes.features.common.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.PhotoGalleryItemBinding
import pl.jakubokrasa.bikeroutes.databinding.RvPhotoItemBinding
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable

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
//    override fun isViewFromObject(view: View, obj: Any): Boolean {
//        return view == obj
//    }
//
//    override fun getCount(): Int {
//        return photos.size
//    }
//
//    override fun instantiateItem(container: ViewGroup, position: Int): Any {
//        val inflater = LayoutInflater.from(context)
//        val view = inflater.inflate(R.layout.photo_gallery_item,null)
//        val imageView = view.findViewById(R.id.photo_gallery_item_image) as ImageView
//        Glide.with(context).load(photos[position])
//            .placeholder(R.drawable.ic_baseline_photo_24)
//            .apply(RequestOptions().centerInside())
//            .into(imageView)
//        val vp = container as ViewPager
//        vp.addView(view,0)
//        return view
//
//
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
//        //  super.destroyItem(container, position, `object`)
//        val viewPager = container as ViewPager
//        val view = obj as View
//        viewPager.removeView(view)
//    }

    inner class PhotoGalleryViewHolder(private val binding: PhotoGalleryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(photo: PhotoInfoDisplayable) {
            with(binding) {
                Glide.with(context)
                    .load(photo.reference)
                    .placeholder(R.drawable.ic_baseline_photo_24)
                    .apply(RequestOptions().centerInside())
                    .into(galleryImage)
            }
        }
    }

}