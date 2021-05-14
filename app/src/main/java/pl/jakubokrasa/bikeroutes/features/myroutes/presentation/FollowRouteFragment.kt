package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.android.ext.android.inject
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.util.LocationUtils
import pl.jakubokrasa.bikeroutes.core.util.configureOsmDroid
import pl.jakubokrasa.bikeroutes.core.util.routeColor
import pl.jakubokrasa.bikeroutes.databinding.FragmentFollowRouteBinding
import pl.jakubokrasa.bikeroutes.features.map.domain.LocationService
import pl.jakubokrasa.bikeroutes.features.map.presentation.MapFragment.Companion.SEND_LOCATION_ACTION
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable


class FollowRouteFragment : BaseFragment(R.layout.fragment_follow_route) {

    private var _binding: FragmentFollowRouteBinding? = null
    private val binding get() = _binding!!
    private lateinit var route: RouteDisplayable
    private lateinit var points: List<PointDisplayable>
    private val polyline = Polyline()
    private val mLocalBR: LocalBroadcastManager by inject()
    private lateinit var mPreviousLocMarker: Marker

    private var followLocationMode = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowRouteBinding.bind(view)
        configureOsmDroid(requireContext())
        requireActivity().startService(Intent(context, LocationService::class.java))
        LocationUtils(activity as Activity).enableGpsIfNecessary()

        updateToolbar()
        showRoute(view)

        binding.btShowLocation.setOnClickListener(btShowLocationOnClick)
    }

    override fun onStart() {
        super.onStart()
        val locFilter = IntentFilter(SEND_LOCATION_ACTION)
        mLocalBR.registerReceiver(locationServiceReceiver, locFilter)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume();
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mLocalBR.unregisterReceiver(locationServiceReceiver)
        stopLocationService()
    }

    private fun showRoute(view: View) {
        view.post {
            setRoute()
            setPoints()
            updateRouteInfo()
            setPolylineProperties()
            setMapViewProperties(setZoom = false)
            binding.mapView.invalidate()
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
                                showToast("Feature not yet implemented")
//                                viewModel.deleteRoute(route.toRoute())
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
        polyline.setPoints(points.map { p -> p.geoPoint })
        if (!polyline.isEnabled) polyline.isEnabled = true //we get the location for the first time
        polyline.outlinePaint.strokeWidth = 7F
        polyline.outlinePaint.color = routeColor
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
        if(followLocationMode) binding.mapView.controller.animateTo(geoPoint)
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

    @SuppressLint("ClickableViewAccessibility")
    private val btShowLocationOnClick = View.OnClickListener {
        binding.mapView.controller.animateTo(mPreviousLocMarker.position)
        followLocationMode = true
        binding.mapView.setOnTouchListener { _, _ ->
            followLocationMode = false //todo to się wykonuje za każdym dotknięciem. Można spróbować tego uniknąć https://stackoverflow.com/a/6619160/9343040
            false // todo co tu oznacza false?
        }
    }

    private fun stopLocationService() {
        requireActivity().stopService(Intent(requireContext(), LocationService::class.java))
    }

    private fun setRoute() {
        arguments
            ?.getParcelable<RouteDisplayable>(ROUTE_TO_FOLLOW_KEY)
            ?.let { route = it}
    }

    private fun setPoints() {
        val serializable = arguments
            ?.getSerializable(POINTS_TO_FOLLOW_KEY) //I use Serializable instead of Parcelable because I didn't find any simple solution to pass a List through Parcelable
                if(serializable is List<*>?) {
                    serializable.let {
                        points = serializable as List<PointDisplayable>
                    }
                }
    }

    companion object {
        const val ROUTE_TO_FOLLOW_KEY = "routeToFollowKey"
        const val POINTS_TO_FOLLOW_KEY = "pointsToFollowKey"
    }
}