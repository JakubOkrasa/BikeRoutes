package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.jakubokrasa.bikeroutes.databinding.RvMyroutesItemBinding
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.model.RouteWithPointsDisplayable

class MyRoutesRecyclerAdapter() : RecyclerView.Adapter<MyRoutesRecyclerAdapter.MyRoutesViewHolder>() {

    var onItemClick: ((RouteWithPointsDisplayable) -> Unit)? = null
    private var routes = mutableListOf<RouteWithPointsDisplayable>()

    fun setItems(routes: List<RouteWithPointsDisplayable>) {
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

        fun bind(route: RouteWithPointsDisplayable) {
            with(binding) {
                tvPointcount.text = String.format("%d", route.points.size)
                tvName.text = String.format("%s", route.name)
                tvDescription.text = String.format("%s", route.description)

                if (route.distance < 1_000) tvDistance.text = String.format("%dm", route.distance)
                else tvDistance.text = String.format("%.1fkm", (route.distance / 1_000.0).toFloat())
            }
        }
        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(routes[adapterPosition])
            }
        }
    }

}