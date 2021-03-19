package pl.jakubokrasa.bikeroutes.features.myroutes.ui

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extentions.inflate
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.model.RouteWithPointsDisplayable

class MyRoutesRecyclerAdapter : RecyclerView.Adapter<MyRoutesRecyclerAdapter.ViewHolder>() {

    private val routes = mutableListOf<RouteWithPointsDisplayable>();
//    constructor(routes: MutableList<RouteWithPointsDisplayable>) : this() {
//        this.routes = routes
//    }

    public fun setItems(routes: List<RouteWithPointsDisplayable>) {
        if (routes.isNotEmpty()) this.routes.clear()

        this.routes.addAll(routes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRoutesRecyclerAdapter.ViewHolder {
        return ViewHolder(parent.
            inflate(R.layout.fragment_browseroutes_rv_item_row))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val route = routes[position]
        holder.bind(route)
//        holder.textView.text = Integer.toString(routes[position].points.size)
    }

    override fun getItemCount(): Int {
        return routes.size
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        private val binding = ItemRouteBinding.bind(itemView)
        private val v = view

        fun bind(route: RouteWithPointsDisplayable) {
            val tvDebug: TextView = v.findViewById(R.id.tv_pointcount)
            val tvName: TextView = v.findViewById(R.id.tv_name)
            val tvDistance: TextView = v.findViewById(R.id.tv_distance)

            tvDebug.text = String.format("%d", route.points.size)
            tvName.text = "Test name"
//            tvDistance.text =

        }

        init {
            // Define click listener for the ViewHolder's View.
        }
    }


}