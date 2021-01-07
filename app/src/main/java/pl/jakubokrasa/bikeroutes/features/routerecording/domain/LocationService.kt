package pl.jakubokrasa.bikeroutes.features.routerecording.domain

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.MainActivity
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.util.LocationUtils
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RecordRouteFragment
import pl.jakubokrasa.bikeroutes.features.routerecording.ui.RecordRouteFragment.Companion.SEND_LOCATION_ACTION

@KoinApiExtension
class LocationService : Service(), KoinComponent {
//    private val track = Polyline()
    private val mLocationRequest: LocationRequest by inject()
    private val mFusedLocationClient: FusedLocationProviderClient by inject()
    private val locUtils: LocationUtils by inject()
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mServiceHandler: Handler
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var mLocation: Location


    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        Log.i(LOG_TAG, "onCreate")
        getLastLocation()
        val handlerThread = HandlerThread(LOG_TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        createNotificationChannel()
        super.onCreate()

    }

    private fun locationCallbackInit() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
    }



    private fun createNotificationChannel() {
        val notifyManager =
            baseContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_DEFAULT_IMPORTANCE,
                "Location Service notification",
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "the notification informs about running Location service"
            notifyManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(LOG_TAG, "onStartCommand")
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
            .setContentTitle("Location Service").setContentText("is running")
            .setSmallIcon(R.drawable.ic_loc_service).setContentIntent(pendingIntent)
            .setTicker("Location Service started (ticker)").build()

        startForeground(SERVICE_NOTIFICATION_ID, notification) //todo w przykladzie to jest w onUnBind (wtedy notifikacja jest widoczna tylko gdy aplikacja jest zminimalizowana
        requestLocationUpdates()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        Log.d(LOG_TAG, "onDestroy")
        mServiceHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    fun requestLocationUpdates() {
        Log.i(LOG_TAG, "Requesting location updates")
        Log.d(LOG_TAG, "service request updates Thread: ${Thread.currentThread().name}")
        locUtils.setRequestingLocationUpdates(this, true)
        locationCallbackInit()
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                Looper.myLooper())
        } catch (unlikely: SecurityException) {
            locUtils.setRequestingLocationUpdates(this, false)
            Log.e(LOG_TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    mLocation = task.result
                } else {
                    Log.w(LOG_TAG, "Failed to get location.")
                }
            }
        } catch (unlikely: SecurityException) {
            Log.e(LOG_TAG, "Lost location permission.$unlikely")
        }
    }

    private fun onNewLocation(loc: Location) {
//        Log.i(LOG_TAG, "New location: $loc")
        Log.d(LOG_TAG, "service Thread: ${Thread.currentThread().name}")
        mLocation = loc
        recordCurrentLocationInTrack(GeoPoint(loc.latitude, loc.longitude))

        // Notify anyone listening for broadcasts about the new location.
//        val intent =
//            Intent(ACTION_BROADCAST)
//        intent.putExtra(EXTRA_LOCATION, location)
//        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        // Update notification content if running as a foreground service.
//        if (serviceIsRunningInForeground(this)) {
//            mNotificationManager.notify(NOTIFICATION_ID, getNotification())
//        }
    }

    private fun recordCurrentLocationInTrack(newLocation: GeoPoint) {
        Log.i(LOG_TAG, "POINT: lat: ${newLocation.latitude}, lng: ${newLocation.longitude}")

        //send update UI broadcast
        val newLocIntent = Intent()
        newLocIntent.action = SEND_LOCATION_ACTION
        newLocIntent.putExtra("EXTRA_LAT", newLocation.latitude)
        newLocIntent.putExtra("EXTRA_LNG", newLocation.longitude)
        sendBroadcast(newLocIntent)
    }



    private fun removeLocationUpdates() {
        Log.i(LOG_TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            locUtils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (unlikely: SecurityException) {
            locUtils.setRequestingLocationUpdates(this, true);
            Log.e(LOG_TAG, "Lost location permission. Could not remove updates. $unlikely");
        }
    }



    companion object {
        const val CHANNEL_DEFAULT_IMPORTANCE = "service_notification_channel"
        const val SERVICE_NOTIFICATION_ID = 1
        val LOG_TAG: String = LocationService::class.java.simpleName


    }
}