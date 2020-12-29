package pl.jakubokrasa.bikeroutes.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.NetworkLocationIgnorer
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import pl.jakubokrasa.bikeroutes.R


class RecordRouteFragment : Fragment(), LocationListener {

    private lateinit var recordRouteViewModel: RecordRouteViewModel
    private lateinit var map: MapView
    private var track: Polyline = Polyline()
    private lateinit var mRotationGestureOverlay: Overlay
    private var trackPointsList: ArrayList<GeoPoint> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recordRouteViewModel = ViewModelProvider(this).get(RecordRouteViewModel::class.java)
        Configuration.getInstance()
            .load(context, getDefaultSharedPreferences(context)) //osmdroid config
        val root = inflater.inflate(R.layout.fragment_record_route, container, false)
//        val textView: TextView = root.findViewById(R.id.tv_record_route)
//        recordRouteViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        map = root.findViewById(R.id.mv_record_route)
        map.setTileSource(TileSourceFactory.MAPNIK)

        requestPermissionsIfNecessary(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

        val locManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (arePermissionGranted(
                arrayOf( //todo czy to sprawdzenie jest potrzebne? A może użyć onRequestPermissionsResult dostępne w Activity lub FragmentActivity
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            )
        ) {
            locManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_LOCATION_LISTENER_TIME_MS,
                MIN_LOCATION_LISTENER_DISTANCE_M,
                this
            )
            setCurrentLocation()
        }

        map.setMultiTouchControls(true)

        track.outlinePaint.strokeWidth = 2F



        return root
    }


    private fun setCurrentLocation() {
        val locProvider = GpsMyLocationProvider(context)
        val locationOverlay = MyLocationNewOverlay(locProvider, map)
        locationOverlay.enableFollowLocation()
        map.overlays.add(locationOverlay)
//        map.controller.setCenter()
//        map.controller.animateTo(locProvider)
        map.controller.setZoom(18.0)
    }

    private fun requestPermissionsIfNecessary(permissions: Array<String>) {
        val permissionsToRequest: ArrayList<String> = ArrayList()
        for (perm in permissions) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    perm
                ) != PackageManager.PERMISSION_GRANTED
            ) { //todo check if context is from fragment (look at 2. comment https://stackoverflow.com/a/40760760/9343040)
                permissionsToRequest.add(perm)
            }
        }
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
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
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        val permissionsToRequest: ArrayList<String?> = ArrayList()
        for (i in grantResults.indices) {
            permissionsToRequest.add(permissions[i])
        }
        if (permissionsToRequest.isNotEmpty()) {
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    val mIgnorer = NetworkLocationIgnorer()
    var mLastTime: Long = 0 // milliseconds
    override fun onLocationChanged(location: Location) {
        val currentTime = System.currentTimeMillis()
        if (mIgnorer.shouldIgnore(location.provider, currentTime)) return
        val dT: Double = currentTime - mLastTime.toDouble()
        if (dT < 100.0) {
            //Toast.makeText(this, pLoc.getProvider()+" dT="+dT, Toast.LENGTH_SHORT).show();
            return
        }
        mLastTime = currentTime

        Log.d(LOG_TAG, "altitude: ${location.altitude}")
        val lat = location.latitude * 1E6
        val lng = location.longitude * 1E6
        recordCurrentLocationInTrack(GeoPoint(lat, lng))


    }

    private fun recordCurrentLocationInTrack(newLocation: GeoPoint) {
        val modifiedGeoPoint: GeoPoint = GeoPoint(
            newLocation.latitude / 1000000, // without division by 1 000 000, there is the IllegalArgumentException: north must be in [-85.05112877980659,85.05112877980659] https://github.com/osmdroid/osmdroid/issues/1206
            newLocation.longitude / 1000000
        )
        track.addPoint(modifiedGeoPoint)
        Log.i("RecordRouteFragment","POINT: lat: ${modifiedGeoPoint.latitude}, lng: ${modifiedGeoPoint.longitude}")

        //update UI
        if (!track.isEnabled) {
            //we get the location for the first time:
            track.isEnabled = true
        }
        map.controller.animateTo(newLocation)
        map.overlayManager.add(track)
        map.invalidate()
    }


    override fun onProviderEnabled(provider: String) {}

    override fun onProviderDisabled(provider: String) {}

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.d(LOG_TAG, "location listener status: $status")
    }


    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 0
        private val LOG_TAG: String? = RecordRouteFragment::class.simpleName
        const val MIN_LOCATION_LISTENER_TIME_MS = 200L
        const val MIN_LOCATION_LISTENER_DISTANCE_M = 1f

    }

}