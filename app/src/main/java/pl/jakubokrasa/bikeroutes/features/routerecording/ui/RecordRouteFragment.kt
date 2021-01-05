package pl.jakubokrasa.bikeroutes.features.routerecording.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinApiExtension
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.NetworkLocationIgnorer
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.LocationService


@KoinApiExtension
class RecordRouteFragment : Fragment()
{

    private lateinit var recordRouteViewModel: RecordRouteViewModel
    private lateinit var map: MapView
    private var track: Polyline = Polyline()
    private lateinit var mRotationGestureOverlay: Overlay
    private var trackPointsList: ArrayList<GeoPoint> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        requireActivity().startService(Intent(context, LocationService::class.java))
        recordRouteViewModel = ViewModelProvider(this).get(RecordRouteViewModel::class.java)
        Configuration.getInstance().load(context, getDefaultSharedPreferences(context)) //osmdroid config
        val root = inflater.inflate(R.layout.fragment_record_route, container, false)
        map = root.findViewById(R.id.mv_record_route) // TODO: 12/29/2020 replace with view binding
        map.setTileSource(TileSourceFactory.MAPNIK)
        requestPermissionsIfNecessary(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE))

//        requestLocationUpdatesIfPermsAreGranted()
//        setCurrentLocation()

        val recordTrackBt = root.findViewById<Button>(R.id.bt_record)
        recordTrackBt.setOnClickListener() {

            if (!arePermissionGranted(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ))) {
                requestPermissions(arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ), REQUEST_PERMISSIONS_REQUEST_CODE)
            } else {
                val locService: LocationService by inject()
                locService.requestLocationUpdates()
            }
        }

        map.setMultiTouchControls(true)
        track.outlinePaint.strokeWidth = 2F


        return root
    }

//    private fun requestLocationUpdatesIfPermsAreGranted() {
//        if (arePermissionGranted(
//                arrayOf( //todo czy to sprawdzenie jest potrzebne? A może użyć onRequestPermissionsResult dostępne w Activity lub FragmentActivity
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                )
//            )
//        ) {
//            val locManager: LocationManager by inject()
//            locManager.requestLocationUpdates(
//                LocationManager.GPS_PROVIDER,
//                MIN_LOCATION_LISTENER_TIME_MS,
//                MIN_LOCATION_LISTENER_DISTANCE_M,
//                this
//            )
//        }
//    }


//    private fun setCurrentLocation() {
//        val locProvider = GpsMyLocationProvider(context)
//        val locationOverlay = MyLocationNewOverlay(locProvider, map)
//        locationOverlay.enableFollowLocation()
//        map.overlays.add(locationOverlay)
////        map.controller.setCenter()
////        map.controller.animateTo(locProvider)
//        map.controller.setZoom(18.0)
//    }

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

    private fun arePermissionGranted(permissions: Array<String>): Boolean {
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    perm
                ) != PackageManager.PERMISSION_GRANTED
            ) return false
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    override fun onPause() {
        super.onPause()
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause() //needed for compass, my location overlays, v6.0.0 and up
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

    val mIgnorer = NetworkLocationIgnorer()
    var mLastTime: Long = 0 // milliseconds
//    override fun onLocationChanged(location: Location) {
//        val currentTime = System.currentTimeMillis()
//        if (mIgnorer.shouldIgnore(location.provider, currentTime)) return
//        val dT: Double = currentTime - mLastTime.toDouble()
//        if (dT < 100.0) {
//            //Toast.makeText(this, pLoc.getProvider()+" dT="+dT, Toast.LENGTH_SHORT).show();
//            return
//        }
//        mLastTime = currentTime
//
//        Log.d(LOG_TAG, "altitude: ${location.altitude}")
//        val lat = location.latitude
//        val lng = location.longitude
//        recordCurrentLocationInTrack(GeoPoint(lat, lng))
//
//
//    }

//    private fun recordCurrentLocationInTrack(newLocation: GeoPoint) {
//        track.addPoint(newLocation)
//        Log.i("RecordRouteFragment","POINT: lat: ${modifiedGeoPoint.latitude}, lng: ${modifiedGeoPoint.longitude}")
//
//        //update UI
//        if (!track.isEnabled) {
//            //we get the location for the first time:
//            track.isEnabled = true
//        }
//        map.controller.animateTo(newLocation)
//        map.overlayManager.add(track)
//        map.invalidate()
//    }


//    override fun onProviderEnabled(provider: String) {}
//
//    override fun onProviderDisabled(provider: String) {}
//
//    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
//        Log.d(LOG_TAG, "location listener status: $status")
//    }


    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 0
        private val LOG_TAG: String? = RecordRouteFragment::class.simpleName
        const val MIN_LOCATION_LISTENER_TIME_MS = 200L
        const val MIN_LOCATION_LISTENER_DISTANCE_M = 1f

    }

}