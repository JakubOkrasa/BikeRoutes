package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.jakubokrasa.bikeroutes.core.util.getFormattedDistance
import pl.jakubokrasa.bikeroutes.core.util.getFormattedRideTime
import pl.jakubokrasa.bikeroutes.databinding.RvMyroutesItemBinding
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable

class MyRoutesRecyclerAdapter() : RecyclerView.Adapter<MyRoutesRecyclerAdapter.MyRoutesViewHolder>() {

    var onItemClick: ((RouteDisplayable) -> Unit)? = null
    private var routes = mutableListOf<RouteDisplayable>()

    fun setItems(routes: List<RouteDisplayable>) {
        if (routes.isNotEmpty()) this.routes.clear()
        this.routes.addAll(routes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRoutesRecyclerAdapter.MyRoutesViewHolder {
        val binding = RvMyroutesItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MyRoutesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyRoutesViewHolder, position: Int) {
        val route = routes[position]
        holder.bind(route)
    }

    override fun getItemCount(): Int {
        return routes.size
    }

    inner class MyRoutesViewHolder(private val binding: RvMyroutesItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(route: RouteDisplayable) {
            with(binding) {
                tvName.text = String.format("%s", route.name)
                tvDescription.text = String.format("%s", route.description)
                tvDistance.text = getFormattedDistance(route.distance)
                tvRidetime.text = getFormattedRideTime(route.rideTimeMinutes)
            }
        }
        init {
            // Define click listener for the MyRoutesViewHolder's View.
            itemView.setOnClickListener {
                onItemClick?.invoke(routes[adapterPosition])
            }
        }
    }

}