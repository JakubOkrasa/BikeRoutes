package pl.jakubokrasa.bikeroutes.features.ui_features.sharedroutes.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.jakubokrasa.bikeroutes.core.util.getFormattedDistance
import pl.jakubokrasa.bikeroutes.core.util.getFormattedRideTime
import pl.jakubokrasa.bikeroutes.databinding.RvSharedroutesItemBinding
import pl.jakubokrasa.bikeroutes.features.routes.presentation.model.RouteDisplayable

class SharedRoutesRecyclerAdapter: RecyclerView.Adapter<SharedRoutesRecyclerAdapter.SharedRoutesViewHolder>() {

    var onItemClick: ((RouteDisplayable) -> Unit)? = null
    private var routes = mutableListOf<RouteDisplayable>()

    fun setItems(routes: List<RouteDisplayable>) {
        if (routes.isNotEmpty()) this.routes.clear()
        this.routes.addAll(routes.sortedByDescending { it.createdAt })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedRoutesRecyclerAdapter.SharedRoutesViewHolder {
        val binding = RvSharedroutesItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return SharedRoutesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SharedRoutesViewHolder, position: Int) {
        val route = routes[position]
        holder.bind(route)
    }

    override fun getItemCount(): Int {
        return routes.size
    }

    inner class SharedRoutesViewHolder(private val binding: RvSharedroutesItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(route: RouteDisplayable) {
            with(binding) {
                tvName.text = String.format("%s", route.name)
                tvDescription.text = String.format("%s", route.description)
                tvDistance.text = getFormattedDistance(route.distance)
                tvRideTime.text = getFormattedRideTime(route.rideTimeMinutes)
            }
        }
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(routes[adapterPosition])
            }
        }
    }

}