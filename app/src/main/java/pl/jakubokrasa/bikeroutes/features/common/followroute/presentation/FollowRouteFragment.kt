package pl.jakubokrasa.bikeroutes.features.common.followroute.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.res.ResourcesCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseFragment
import pl.jakubokrasa.bikeroutes.core.util.*
import pl.jakubokrasa.bikeroutes.core.util.enums.MapMode
import pl.jakubokrasa.bikeroutes.databinding.FragmentFollowrouteBinding
import pl.jakubokrasa.bikeroutes.core.service.LocationService
import pl.jakubokrasa.bikeroutes.features.map.presentation.MapFragment.Companion.SEND_LOCATION_ACTION
import pl.jakubokrasa.bikeroutes.features.common.points.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.common.routes.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment.Companion.POINTS_BUNDLE_KEY
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment.Companion.ROUTE_BUNDLE_KEY


class FollowRouteFragment : BaseFragment<MyRoutesViewModel>(R.layout.fragment_followroute) {

    override val viewModel: MyRoutesViewModel by sharedViewModel()
    private var _binding: FragmentFollowrouteBinding? = null
    private val binding get() = _binding!!
    private lateinit var route: RouteDisplayable
    private lateinit var points: List<PointDisplayable>
    private val polyline = Polyline()
    private val mLocalBR: LocalBroadcastManager by inject()
    private lateinit var mPreviousLocMarker: Marker

    private var mapMode = MapMode.MoveFreely

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowrouteBinding.bind(view)
        configureOsmDroid(requireContext())
        requireActivity().startService(Intent(context, LocationService::class.java))
        LocationUtils(activity as Activity).enableGpsIfNecessary()

        showRoute(view)

        binding.btShowLocation.setOnClickListener(btShowLocationOnClick)
        binding.btRecenter.setOnClickListener(btRecenterOnClick)
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
            setPolylineProperties()
            setMapViewProperties()
            disableFollowingLocation()
            binding.mapView.invalidate()
        }
    }

    private fun setMapViewProperties() {
        with(binding.mapView) {
            setBaseMapViewProperties()
            overlayManager.add(polyline)
            recenterRoute()
        }
    }

    private fun MapView.recenterRoute() {
        zoomToBoundingBox(polyline.bounds, false, 40, 18.0, 0)
    }

    private fun MapView.recenterRouteWithAnimation() {
        zoomToBoundingBox(polyline.bounds, true, 40, 18.0, 1000)
    }

    private fun setPolylineProperties() {
        polyline.setPoints(points.map { p -> p.geoPoint })
        if (!polyline.isEnabled) polyline.isEnabled = true //we get the location for the first time
        polyline.outlinePaint.strokeWidth = routeWidth
        polyline.outlinePaint.color = routeColor
    }

    private fun newLocationUpdateUI(geoPoint: GeoPoint) {
        showCurrentLocationMarker(geoPoint)
        if(mapMode == MapMode.FollowLocation) binding.mapView.controller.animateTo(geoPoint)
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
        enableFollowingLocation()
        binding.mapView.setOnTouchListener { _, _ ->
            disableFollowingLocation()
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val btRecenterOnClick = View.OnClickListener {
        binding.mapView.recenterRouteWithAnimation()
    }

    private fun disableFollowingLocation() {
        mapMode = MapMode.MoveFreely
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun enableFollowingLocation() {
        mapMode = MapMode.FollowLocation
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun stopLocationService() {
        requireActivity().stopService(Intent(requireContext(), LocationService::class.java))
    }

    private fun setRoute() {
        arguments
            ?.getParcelable<RouteDisplayable>(ROUTE_BUNDLE_KEY)
            ?.let { route = it}
    }

    private fun setPoints() {
        val serializable = arguments
            ?.getSerializable(POINTS_BUNDLE_KEY) //I use Serializable instead of Parcelable because I didn't find any simple solution to pass a List through Parcelable
        if(serializable is List<*>?) {
            serializable.let {
                points = serializable as List<PointDisplayable>
            }
        }
    }
}