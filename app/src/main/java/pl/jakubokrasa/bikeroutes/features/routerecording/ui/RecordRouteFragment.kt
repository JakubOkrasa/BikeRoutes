package pl.jakubokrasa.bikeroutes.features.routerecording.ui

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.BuildConfig
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.FragmentRecordRouteBinding
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.LocationService
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.model.RouteDisplayable
import kotlin.collections.ArrayList


class RecordRouteFragment() : Fragment(R.layout.fragment_record_route), KoinComponent {
    private var polyline: Polyline = Polyline()
    private lateinit var mRotationGestureOverlay: Overlay
    private var trackPointsList: ArrayList<GeoPoint> = ArrayList()
    private val mLocalBR: LocalBroadcastManager by inject()
    private val viewModel: RecordRouteViewModel by viewModel()

    //from https://developer.android.com/topic/libraries/view-binding
    private var _binding: FragmentRecordRouteBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRecordRouteBinding.bind(view)
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requestPermissionsIfNecessary(OSM_PERMISSIONS)
        Configuration.getInstance().load(context, getDefaultSharedPreferences(context)) //osmdroid config

        binding.btStartRecord.setOnClickListener(btRecordRouteOnClick)
        binding.btStopRecord.setOnClickListener(btStopRecordOnClick)


        setMapViewProperties()
        setPolylineProperties()
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val root = binding.root
//
//
//        return root
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//    }

   override fun onStart() {
        super.onStart()
        val locFilter = IntentFilter(SEND_LOCATION_ACTION)
        mLocalBR.registerReceiver(locationServiceReceiver, locFilter)
    }

    override fun onStop() {
        super.onStop()
        mLocalBR.unregisterReceiver(locationServiceReceiver)
    }

    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        binding.mapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        binding.mapView.onPause() //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        val permissionsToRequest: ArrayList<String?> = ArrayList()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
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
            requestPermissions(permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE)
        }
    }

    private fun newLocationUpdateUI(newLocation: GeoPoint) {
        viewModel.insertCurrentPoint(newLocation)
        if (!polyline.isEnabled) polyline.isEnabled = true //we get the location for the first time:
        observeCurrentRoute()
        binding.mapView.controller.animateTo(newLocation)
        binding.mapView.overlayManager.add(polyline)
        binding.mapView.invalidate()
//        Log.d(LOG_TAG, "Thread: ${Thread.currentThread().name}")
    }

    private fun stopLocationService() {
        requireActivity().stopService(Intent(requireContext(), LocationService::class.java))
    }

    private fun observeCurrentRoute() {
        viewModel.route.observe(viewLifecycleOwner, {
            val geoPoints = it.points.map { point -> point.geoPoint }
            polyline.setPoints(geoPoints)
        })
    }

    private fun setMapViewProperties() {
        binding.mapView.setTileSource(TileSourceFactory.MAPNIK)
        binding.mapView.setMultiTouchControls(true)
        binding.mapView.controller.setZoom(18.0)
    }

    private fun setPolylineProperties() {
        polyline.outlinePaint.strokeWidth = 7F
        polyline.outlinePaint.color = Color.MAGENTA
    }

    private val locationServiceReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val loc = intent!!.getParcelableExtra<Location>("EXTRA_LOCATION")
            loc?.let {
                binding.tvAccuracy.text = loc.accuracy.toString()
                newLocationUpdateUI(GeoPoint(loc.latitude, loc.longitude))
            }
        }
    }

    private val btStopRecordOnClick = View.OnClickListener()  {
        stopLocationService()
        binding.btStopRecord.visibility = View.GONE
        binding.btStartRecord.visibility = View.VISIBLE
    }

    private val btRecordRouteOnClick = View.OnClickListener() {
        viewModel.insertNewRoute(RouteDisplayable(5, true, ArrayList()).toRoute()) // TODO: 2/17/2021 now routeId is hardcoded
        requireActivity().startService(Intent(context, LocationService::class.java))
        observeCurrentRoute()
        binding.btStartRecord.visibility = View.GONE
        binding.btStopRecord.visibility = View.VISIBLE
    }

    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 0
        private val LOG_TAG: String? = RecordRouteFragment::class.simpleName
        val OSM_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        const val SEND_LOCATION_ACTION = BuildConfig.APPLICATION_ID + ".send_location_action"
    }
}
