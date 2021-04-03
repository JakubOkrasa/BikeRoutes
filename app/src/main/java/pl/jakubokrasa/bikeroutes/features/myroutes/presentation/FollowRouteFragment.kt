package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.inject
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.FragmentFollowRouteBinding
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.LocationService
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.MapFragment.Companion.SEND_LOCATION_ACTION
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RouteViewModel
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.model.RouteWithPointsDisplayable


class FollowRouteFragment : Fragment(R.layout.fragment_follow_route) {

    private var _binding: FragmentFollowRouteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RouteViewModel by sharedViewModel()
    private lateinit var route: RouteWithPointsDisplayable
    private val polyline = Polyline()
    private val mLocalBR: LocalBroadcastManager by inject()
    private lateinit var mPreviousLocMarker: Marker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowRouteBinding.bind(view)
        requireActivity().startService(Intent(context, LocationService::class.java))

        updateToolbar()
        showRoute(view)

    }

    override fun onStart() {
        super.onStart()
        val locFilter = IntentFilter(SEND_LOCATION_ACTION)
        mLocalBR.registerReceiver(locationServiceReceiver, locFilter)
    }

    private fun showRoute(view: View) {
        view.post {
            parentFragmentManager.setFragmentResultListener("requestKey",
                viewLifecycleOwner,
                { _, bundle ->
                    route = bundle.getSerializable("route") as RouteWithPointsDisplayable
                    updateRouteInfo()

                    setPolylineProperties()
                    setMapViewProperties(setZoom = false)
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
        binding.mapView.zoomToBoundingBox(polyline.bounds, false, 18, 25.0, 0)
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

    private fun newLocationUpdateUI(geoPoint: GeoPoint) {
        showCurrentLocationMarker(geoPoint)
        binding.mapView.invalidate()
    }

    private fun showCurrentLocationMarker(geoPoint: GeoPoint) {
        val currentLocMarker = Marker(binding.mapView, context)
        currentLocMarker.icon = ResourcesCompat.getDrawable(resources,
            R.drawable.marker_my_location,
            null)
        currentLocMarker.position = GeoPoint(geoPoint)
        currentLocMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
        if(this::mPreviousLocMarker.isInitialized) binding.mapView.overlays.remove(
            mPreviousLocMarker)
        binding.mapView.overlays.add(currentLocMarker)
        mPreviousLocMarker = currentLocMarker
    }

    private val locationServiceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val loc = intent!!.getParcelableExtra<Location>("EXTRA_LOCATION")
            loc?.let {
                newLocationUpdateUI(GeoPoint(loc))
            }
        }
    }

}