package pl.jakubokrasa.bikeroutes.features.reviews.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.jakubokrasa.bikeroutes.core.util.getFormattedDate
import pl.jakubokrasa.bikeroutes.core.util.getFormattedUserDisplayName
import pl.jakubokrasa.bikeroutes.databinding.RvReviewItemBinding
import pl.jakubokrasa.bikeroutes.features.reviews.presentation.model.ReviewDisplayable

class ReviewsRecyclerAdapter: RecyclerView.Adapter<ReviewsRecyclerAdapter.ReviewViewHolder>() {
    private var reviews = mutableListOf<ReviewDisplayable>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = RvReviewItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    fun setItems(reviews: List<ReviewDisplayable>) {
        if (reviews.isNotEmpty()) this.reviews.clear()
        this.reviews.addAll(reviews)
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(
        private val binding: RvReviewItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(review: ReviewDisplayable) {
                with(binding) {
                    tvAuthor.text = getFormattedUserDisplayName(review.userId)
                    tvDate.text = getFormattedDate(review.createdAt)
                    tvContent.text = review.content
                }
            }
        }
    }