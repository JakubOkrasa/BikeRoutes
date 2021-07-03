package pl.jakubokrasa.bikeroutes.features.common.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import pl.jakubokrasa.bikeroutes.databinding.RvPhotoItemBinding
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable


class PhotosRecyclerAdapter: RecyclerView.Adapter<PhotosRecyclerAdapter.PhotosViewHolder>() {

    var onItemClick: ((PhotoInfoDisplayable) -> Unit)? = null
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
//                tvTest.text = photo.reference.substringAfterLast('/')
                val imageView = ImageView(context)
//                Glide.with(context).load(photo.reference).into(imageView)
                linearLayout.addView(imageView)

//                val token = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjhmNDMyMDRhMTc5MTVlOGJlN2NjZDdjYjI2NGRmNmVhMzgzYzQ5YWIiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vYmlrZXJvdXRlcy1mOGE0ZCIsImF1ZCI6ImJpa2Vyb3V0ZXMtZjhhNGQiLCJhdXRoX3RpbWUiOjE2MjUzMDQ2MDYsInVzZXJfaWQiOiJJQUxzU2twd0gyWGRvVHBpaFR0eDN5RVZMc0EzIiwic3ViIjoiSUFMc1NrcHdIMlhkb1RwaWhUdHgzeUVWTHNBMyIsImlhdCI6MTYyNTMwNTc1NCwiZXhwIjoxNjI1MzA5MzU0LCJlbWFpbCI6Imt1YmFvLmZiQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiZmlyZWJhc2UiOnsiaWRlbnRpdGllcyI6eyJlbWFpbCI6WyJrdWJhby5mYkBnbWFpbC5jb20iXX0sInNpZ25faW5fcHJvdmlkZXIiOiJwYXNzd29yZCJ9fQ.Etjqxm5mARLtT4KqTFx8MozvUJkimwSCXQAlpOGHdvKIrswLFG5TngxSf4UT1S9Tar-bULLavU17SSAy01DaJqFGKww43R1TidJudzdfi0sKhs1zwuZnqtqEIiBvEYaSWnH1YxLLltIc4J0iSOzCflBiBNk4XO-q3U_59Y8mxlxcqevDyA19pizR73NTx-NF4N4tpTqIzXSOgFSm8hSz2ocrQSZdmCa0hgJzdMxzhhSZeFim07pHSSvBupsb1zc1YsmBsFoUkUBooN2H8bEbRGym_FS6lh_D4_Bzr9lAsLBxTywrfSWpWrXIdzugRpwSabRLjuKsDX9WTf55clGlCA"
//                val glideUrl =
//                    GlideUrl(photo.reference, LazyHeaders.Builder()
//                        .addHeader("Authorization", "Bearer $token").build())
//
//                Glide.with(context).load(glideUrl).into(imageView)
//                                linearLayout.addView(imageView)

                Glide.with(context)
                    .load("storageReference")
                    .into(imageView)


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