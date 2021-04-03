package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.FragmentFollowRouteBinding
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteViewModel
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.model.RouteWithPointsDisplayable


class FollowRouteFragment : Fragment(R.layout.fragment_follow_route) {

    private var _binding: FragmentFollowRouteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RouteViewModel by sharedViewModel()
    private lateinit var route: RouteWithPointsDisplayable
    private val polyline = Polyline()
    private var first = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowRouteBinding.bind(view)

        updateToolbar()
        showRoute(view)

    }

    private fun showRoute(view: View) {
        view.post {
            parentFragmentManager.setFragmentResultListener("requestKey",
                viewLifecycleOwner,
                { _, bundle ->
                    route = bundle.getSerializable("route") as RouteWithPointsDisplayable
                    updateRouteInfo()

                    //        showCurrentLocationMarker(geoPoint)
                    setMapViewProperties(setZoom = false)
                    setPolylineProperties()
                    binding.mapView.invalidate()
                })
        }
    }

    private fun updateToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_followroute_home)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_followroute_edit -> {
                    clearToolbarMenu()
                    binding.toolbar.inflateMenu(R.menu.menu_followroute_edit)
                    binding.toolbar.setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.action_followroute_done -> {
                                clearToolbarMenu()
                                updateToolbar()
                                true
                            }
                            R.id.action_followroute_remove -> {
                                viewModel.deleteRoute(route.toRoute())
                                true
                            }
                            else -> false
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun updateRouteInfo() {
        binding.tvRouteName.text = route.name
        binding.tvRouteDescription.text = route.description
        binding.tvRouteDistance.text = getFormattedDistance(route.distance)
        binding.tvRouteRideTime.text = "todo ridetime"
    }

    private fun setMapViewProperties(setZoom: Boolean = true) {
        binding.mapView.setTileSource(TileSourceFactory.HIKEBIKEMAP)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.overlayManager.add(polyline)
        binding.mapView.zoomToBoundingBox(polyline.bounds, false, 18)
        if(setZoom) binding.mapView.controller.setZoom(18.0)
    }

    private fun setPolylineProperties() {
        polyline.setPoints(route.points.map { p -> p.geoPoint })
        if (!polyline.isEnabled) polyline.isEnabled = true //we get the location for the first time
        polyline.outlinePaint.strokeWidth = 7F
        polyline.outlinePaint.color = Color.MAGENTA
    }

    private fun getFormattedDistance(distance: Int): String {
        return if (distance < 1_000) String.format("%dm", route.distance)
        else String.format("%.1fkm", (route.distance / 1_000.0).toFloat())
    }

    fun clearToolbarMenu() {
        binding.toolbar.menu.clear()
    }

}