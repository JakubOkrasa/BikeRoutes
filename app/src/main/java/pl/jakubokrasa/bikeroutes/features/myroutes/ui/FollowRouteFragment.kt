package pl.jakubokrasa.bikeroutes.features.myroutes.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.FragmentFollowRouteBinding
import pl.jakubokrasa.bikeroutes.databinding.FragmentMyRoutesBinding
import pl.jakubokrasa.bikeroutes.databinding.FragmentSaveRouteBinding
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RouteViewModel
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.model.RouteWithPointsDisplayable

class FollowRouteFragment : Fragment(R.layout.fragment_follow_route) {

    private var _binding: FragmentFollowRouteBinding? = null
    private val binding get() = _binding!!
//    private val viewModel: RouteViewModel by sharedViewModel()
    private lateinit var route: RouteWithPointsDisplayable
    private val polyline = Polyline()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowRouteBinding.bind(view)

        parentFragmentManager.setFragmentResultListener("requestKey", viewLifecycleOwner, {
             _, bundle ->
            route = bundle.getSerializable("route") as RouteWithPointsDisplayable
            updateRouteInfo()
            polyline.setPoints(route.points.map {p -> p.geoPoint})
                if (!polyline.isEnabled) polyline.isEnabled = true //we get the location for the first time:
                binding.mapView.controller.animateTo(polyline.bounds.centerWithDateLine)
                binding.mapView.overlayManager.add(polyline)
//        showCurrentLocationMarker(geoPoint)
                setMapViewProperties()
                setPolylineProperties()
                binding.mapView.invalidate()
        })

    }

    private fun updateRouteInfo() {
        binding.tvRouteName.text = route.name
        binding.tvRouteDescription.text = route.description
        binding.tvRouteDistance.text = getFormattedDistance(route.distance)
        binding.tvRouteRideTime.text = "todo ridetime"
    }


    private fun setMapViewProperties() {
        binding.mapView.setTileSource(TileSourceFactory.HIKEBIKEMAP)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(18.0)
    }

    private fun setPolylineProperties() {
        polyline.outlinePaint.strokeWidth = 7F
        polyline.outlinePaint.color = Color.MAGENTA
    }

    private fun getFormattedDistance(distance: Int): String {
        return if (distance < 1_000) String.format("%dm", route.distance)
        else String.format("%.1fkm", (route.distance / 1_000.0).toFloat())
    }

}