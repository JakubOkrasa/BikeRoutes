package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.hideKeyboard
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.core.util.*
import pl.jakubokrasa.bikeroutes.core.util.enums.MapMode
import pl.jakubokrasa.bikeroutes.core.util.enums.sharingType
import pl.jakubokrasa.bikeroutes.databinding.FragmentRouteDetailsBinding
import pl.jakubokrasa.bikeroutes.features.common.presentation.CommonRoutesNavigator
import pl.jakubokrasa.bikeroutes.features.map.domain.LocationService
import pl.jakubokrasa.bikeroutes.features.map.presentation.MapFragment.Companion.SEND_LOCATION_ACTION
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable


class RouteDetailsFragment : BaseFragment<MyRoutesViewModel>(R.layout.fragment_route_details) {

    override val viewModel: MyRoutesViewModel by sharedViewModel()

    private var _binding: FragmentRouteDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var route: RouteDisplayable
    private lateinit var points: List<PointDisplayable>
    private val polyline = Polyline()
    private val mLocalBR: LocalBroadcastManager by inject()
    private lateinit var mPreviousLocMarker: Marker
    private lateinit var dialogConfirmRemove: Dialog
    private val navigator: CommonRoutesNavigator by inject()

    private var mapMode = MapMode.moveFreely

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRouteDetailsBinding.bind(view)
        configureOsmDroid(requireContext())
        requireActivity().startService(Intent(context, LocationService::class.java))
        LocationUtils(activity as Activity).enableGpsIfNecessary()

        updateToolbar()
        showRoute(view)

        binding.btFollow.setOnClickListener(btFollowOnClick)

        if(isMyRoute()) dialogConfirmRemove = DialogConfirm(requireContext(), "Are you sure to remove this route?", "remove")
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
            updateRouteInfoLayout()
            updateRouteEditLayout()
            setPolylineProperties()
            setMapViewProperties()
            binding.mapView.invalidate()
        }
    }

    private fun updateToolbar() {
        if (isMyRoute()) {
            binding.toolbar.inflateMenu(R.menu.menu_routedetails_home)
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_routedetails_edit -> {
                        clearToolbarMenu()
                        binding.toolbar.inflateMenu(R.menu.menu_routedetails_edit)
                        binding.toolbar.setOnMenuItemClickListener(toolbarIconsEditModeOnClick)
                        binding.llRouteInfo.makeGone()
                        binding.llVisibility.makeGone()
                        binding.llRouteEdit.makeVisible()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun isMyRoute() = arguments?.getBoolean(IS_MY_ROUTE_KEY) ?: false

	private val toolbarIconsEditModeOnClick = Toolbar.OnMenuItemClickListener {
            insideMenuItem ->
        when (insideMenuItem.itemId) {
            R.id.action_routedetails_done -> {
                updateRouteDisplayableModel()
                viewModel.updateRoute(route)
                updateRouteInfoLayout()
                hideKeyboard()
                binding.llRouteEdit.makeGone()
                binding.llRouteInfo.makeVisible()
                binding.llVisibility.makeVisible()
                clearToolbarMenu()
                updateToolbar()
                true
            }
            R.id.action_routedetails_remove -> {
                dialogConfirmRemove.show()
                true
            }
            else -> false
        }
    }

    private fun updateRouteDisplayableModel() {
        route.name = binding.etRouteName.text.toString()
        route.description = binding.etRouteDescription.text.toString()
        if (binding.swPrivate.isChecked) route.sharingType = sharingType.PRIVATE else route.sharingType = sharingType.PUBLIC
    }

    private val btDialogConfirmOnClick = View.OnClickListener {
        viewModel.removeRouteAndNavBack(route)
        dialogConfirmRemove.dismiss()
    }

    private fun updateRouteInfoLayout() {
        with(binding) {
            tvRouteName.text = route.name

            if(route.description.isBlank()) tvRouteDescription.makeGone()
            else {
                tvRouteDescription.makeVisible()
                tvRouteDescription.text = route.description
            }

            if(route.sharingType == sharingType.PUBLIC)
                tvVisibility.text = "public"
            else if(route.sharingType == sharingType.PRIVATE)
                tvVisibility.text = "only me"


            tvRouteDistance.text = getFormattedDistance(route.distance)
            tvRouteRideTime.text = getFormattedRideTime(route.rideTimeMinutes)

            if(isMyRoute())
                binding.llVisibility.makeVisible()
            else
                binding.llVisibility.makeGone()
        }
    }

    private fun updateRouteEditLayout() {
        binding.etRouteName.setText(route.name)
        if(route.description.isNotBlank()) binding.etRouteDescription.setText(route.description)
        binding.swPrivate.isChecked = run { route.sharingType == sharingType.PRIVATE }
    }

    private fun setMapViewProperties() {
        with(binding.mapView) {
            setTileSource(TileSourceFactory.WIKIMEDIA)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            isTilesScaledToDpi = true
            setMultiTouchControls(true)
            overlayManager.add(polyline)
            zoomToBoundingBox(polyline.bounds, false, 18, 18.0, 0)
            val currentZoom = zoomLevelDouble
            controller.zoomTo(currentZoom-0.5)
        }
    }

    private fun setPolylineProperties() {
        polyline.setPoints(points.map { p -> p.geoPoint })
        if (!polyline.isEnabled) polyline.isEnabled = true //we get the location for the first time
        polyline.outlinePaint.strokeWidth = routeWidth
        polyline.outlinePaint.color = routeColor
    }

    private fun clearToolbarMenu() {
        binding.toolbar.menu.clear()
    }

    private fun newLocationUpdateUI(geoPoint: GeoPoint) {
        showCurrentLocationMarker(geoPoint)
        if(mapMode == MapMode.followLocation) binding.mapView.controller.animateTo(geoPoint)
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

    private fun disableFollowingLocation() {
        mapMode = MapMode.moveFreely
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun enableFollowingLocation() {
        mapMode = MapMode.followLocation
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
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

    private val btFollowOnClick = View.OnClickListener {
        navigator.openFollowRouteFragment(route, points)
    }

    override fun onPendingState() {
        super.onPendingState()
        binding.progressLayout.visibility = View.VISIBLE
    }

    override fun onIdleState() {
        super.onIdleState()
        binding.progressLayout.visibility = View.GONE
    }

    companion object {
        const val ROUTE_TO_FOLLOW_KEY = "routeToFollowKey"
        const val POINTS_TO_FOLLOW_KEY = "pointsToFollowKey"
        const val IS_MY_ROUTE_KEY = "isMyRoutesSourceKey"
    }
}