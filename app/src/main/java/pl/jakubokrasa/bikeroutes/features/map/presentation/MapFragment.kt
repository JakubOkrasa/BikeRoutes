package pl.jakubokrasa.bikeroutes.features.map.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.BuildConfig
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.app.presentation.MainActivity
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_MAPFRAGMENT_MODE_RECORDING
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.core.util.LocationUtils
import pl.jakubokrasa.bikeroutes.core.util.enums.MapMode
import pl.jakubokrasa.bikeroutes.core.util.configureOsmDroid
import pl.jakubokrasa.bikeroutes.core.util.routeColor
import pl.jakubokrasa.bikeroutes.core.util.routeWidth
import pl.jakubokrasa.bikeroutes.databinding.FragmentMapBinding
import pl.jakubokrasa.bikeroutes.features.common.domain.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.map.domain.LocationService
import pl.jakubokrasa.bikeroutes.features.map.navigation.MapFrgNavigator
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable


class MapFragment() : BaseFragment<MapViewModel>(R.layout.fragment_map), KoinComponent {
    private lateinit var polyline: Polyline
    private lateinit var mRotationGestureOverlay: Overlay
    private lateinit var mPreviousLocMarker: Marker
    private val mLocalBR: LocalBroadcastManager by inject()
    private val mapFrgNavigator: MapFrgNavigator by inject()
    override val viewModel: MapViewModel by sharedViewModel()
//    private val currentRouteObserver: Observer<RouteDisplayable!>
    private val requestMultiplePermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        permissions.entries.forEach { Log.d(LOG_TAG, "permission: ${it.key} = ${it.value}") }
    }

    //from https://developer.android.com/topic/libraries/view-binding
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private var mapMode = MapMode.followLocation

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapBinding.bind(view)
        requestPermissionsIfNecessary(OSM_PERMISSIONS)
        configureOsmDroid(requireContext())
        LocationUtils(activity as Activity).enableGpsIfNecessary()
        polyline = Polyline(binding.mapView)
        initObservers()


        binding.btStartRecord.setOnClickListener(btRecordRouteOnClick)
        binding.btStopRecord.setOnClickListener(btStopRecordOnClick)
        binding.btShowLocation.setOnClickListener(btShowLocationOnClick)

        setMapViewProperties()
        setPolylineProperties()

        if(!isRecordingMode()) {
            disableRecordingMode()
            viewModel.deletePoints()
        }

        binding.mapView.setOnTouchListener(mapModeTouchListener)
    }



   override fun onStart() {
        super.onStart()
        val locFilter = IntentFilter(SEND_LOCATION_ACTION)
        mLocalBR.registerReceiver(locationServiceReceiver, locFilter)
    }

    override fun onStop() {
        super.onStop()
        if(!isRecordingMode())
            stopLocationService()
        mLocalBR.unregisterReceiver(locationServiceReceiver)
    }

    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceHelper.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceHelper.getDefaultSharedPreferences(this));
        binding.mapView.onResume() //needed for compass, my location overlays, v6.0.0 and up
        requireActivity().startService(Intent(context, LocationService::class.java))

    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceHelper.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        binding.mapView.onPause() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun initObservers() {
        super.initObservers()
        observePoints()
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest: ArrayList<String> = ArrayList()
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), perm) != PackageManager.PERMISSION_GRANTED
            ) { //todo check if context is from fragment (look at 2. comment https://stackoverflow.com/a/40760760/9343040)
                permissionsToRequest.add(perm)
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            requestMultiplePermissions.launch(permissionsToRequest.toTypedArray())
        }
    }



    private fun newLocationUpdateUI(geoPoint: GeoPoint) {
        if(isRecordingMode()) {
            binding.mapView.overlayManager.add(polyline)
            if (!polyline.isEnabled) polyline.isEnabled = true //we get the location for the first time
        }
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

    private fun stopLocationService() {
        requireActivity().stopService(Intent(requireContext(), LocationService::class.java))
    }

    private fun observePoints() {
        viewModel.getPoints().observe(viewLifecycleOwner, pointsObserver)
    }

    private fun stopObservePoints() {
        viewModel.getPoints().removeObserver(pointsObserver)
    }

    private fun setMapViewProperties() {
        with(binding.mapView) {
            setTileSource(TileSourceFactory.WIKIMEDIA)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
            isTilesScaledToDpi = true
            setMultiTouchControls(true)
            controller.setZoom(18.0)
        }
    }

    private fun setPolylineProperties() {
        polyline.outlinePaint.strokeWidth = routeWidth
        polyline.outlinePaint.color = routeColor
    }

    private val locationServiceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val loc = intent!!.getParcelableExtra<Location>("EXTRA_LOCATION")
            loc?.let {
                if(isRecordingMode()) viewModel.updateDistanceByPrefs(GeoPoint(loc))
                newLocationUpdateUI(GeoPoint(loc))
            }
        }
    }

    private val btShowLocationOnClick = View.OnClickListener {
        binding.mapView.controller.animateTo(mPreviousLocMarker.position)
        mapMode = MapMode.followLocation
    }

    private val btStopRecordOnClick = View.OnClickListener()  {
        disableRecordingMode()
        polyline.setPoints(ArrayList<GeoPoint>()) // strange behaviour: when you make it after stopLocationService(), it doesn't work
        binding.mapView.invalidate()
        with(polyline.bounds) {
            val boundingBoxData = BoundingBoxData(latNorth, latSouth, lonEast, lonWest)
            mapFrgNavigator.openSaveRouteFragment(boundingBoxData)
        }

        preferenceHelper.preferences.edit {
            remove(PREF_KEY_MAPFRAGMENT_MODE_RECORDING)
        }
    }

    private val btRecordRouteOnClick = View.OnClickListener() {
        preferenceHelper.preferences.edit { putInt(PreferenceHelper.PREF_KEY_DISTANCE_SUM, 0)}
        enableRecordingMode()
    }

    @SuppressLint("ClickableViewAccessibility")
    private val mapModeTouchListener = View.OnTouchListener { _, _ ->
        mapMode = MapMode.moveFreely
        false //
    }

    private fun enableRecordingMode() { //todo not a clean practice (public to use it in ViewModel)
        preferenceHelper.preferences.edit {
            putBoolean(PREF_KEY_MAPFRAGMENT_MODE_RECORDING, true)
        }
        observePoints()
        binding.btStartRecord.makeGone()
        binding.btStopRecord.makeVisible()
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun disableRecordingMode() {
        preferenceHelper.preferences.edit {
            putBoolean(PREF_KEY_MAPFRAGMENT_MODE_RECORDING, false)
        }
        stopObservePoints()
        binding.btStopRecord.makeGone()
        binding.btStartRecord.makeVisible()
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun isRecordingMode() = preferenceHelper.preferences.getBoolean(PREF_KEY_MAPFRAGMENT_MODE_RECORDING, false)

    private val pointsObserver = Observer<List<PointDisplayable>> {
        if(it.isNotEmpty())
            polyline.setPoints(it.map { point -> point.geoPoint })
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
        private val LOG_TAG: String? = MapFragment::class.simpleName
        val OSM_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        const val SEND_LOCATION_ACTION = BuildConfig.APPLICATION_ID + ".send_location_action"
        val BOUNDING_BOX_DATA_KEY = "boundingBoxDataKey"

    }
}
