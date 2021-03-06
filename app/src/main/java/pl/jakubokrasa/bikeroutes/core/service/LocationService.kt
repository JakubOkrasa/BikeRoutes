package pl.jakubokrasa.bikeroutes.core.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.edit
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.osmdroid.util.GeoPoint
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.user.presentation.MainActivity
import pl.jakubokrasa.bikeroutes.core.util.PreferenceHelper
import pl.jakubokrasa.bikeroutes.features.map.domain.model.GeoPointData
import pl.jakubokrasa.bikeroutes.features.ui_features.map.presentation.MapFragment.Companion.SEND_LOCATION_ACTION
import pl.jakubokrasa.bikeroutes.features.ui_features.map.presentation.MapViewModel

class LocationService : Service(), KoinComponent {
    private val mLocationRequest: LocationRequest by inject()
    private val mFusedLocationClient: FusedLocationProviderClient by inject()
    private val mLocalBR: LocalBroadcastManager by inject()
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mServiceHandler: Handler
    private lateinit var handlerThread: HandlerThread
    private lateinit var mNotificationManager: NotificationManager
    private lateinit var mLocation: Location
    private val mapViewModel: MapViewModel by inject()
    private val preferenceHelper: PreferenceHelper by inject()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        handlerThread = HandlerThread(LOG_TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

        // useful e.g. when the process with app was killed while the Recording Mode was turned on
        disableRecordingModeInPreferences()
        super.onCreate()

    }

    private fun disableRecordingModeInPreferences() {
        preferenceHelper.preferences.edit {
            putBoolean(PreferenceHelper.PREF_KEY_MAPFRAGMENT_MODE_RECORDING, false)
        }
    }

    private fun locationCallbackInit() {
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.let {
                    val location = it.lastLocation
                    onNewLocation(location)
                }
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
            channel.setSound(null, null)
            notifyManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification = NotificationCompat.Builder(this, CHANNEL_DEFAULT_IMPORTANCE)
            .setContentTitle("Bike Routes")
            .setSmallIcon(R.drawable.ic_loc_service)
            .setContentIntent(pendingIntent)
            .setSound(null)
            .build()

            startForeground(SERVICE_NOTIFICATION_ID, notification)
            requestLocationUpdates()

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null)
        if(this::mLocationCallback.isInitialized)
            removeLocationUpdates()
        super.onDestroy()
    }

    private fun requestLocationUpdates() {
        Log.i(LOG_TAG, "Requesting location updates")
        locationCallbackInit()
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                handlerThread.looper)
        } catch (unlikely: SecurityException) {
            Log.e(LOG_TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    private fun onNewLocation(loc: Location) {
        mLocation = loc

        if(isRecordingMode()) {
            mapViewModel.insertPoint(GeoPointData(loc.latitude, loc.longitude))
            mapViewModel.updateDistance(GeoPoint(loc))
        }

        //send update UI broadcast
        val newLocIntent = Intent()
        newLocIntent.action = SEND_LOCATION_ACTION
        newLocIntent.putExtra("EXTRA_LOCATION", loc)
        mLocalBR.sendBroadcast(newLocIntent)

    }

    private fun isRecordingMode() = preferenceHelper.preferences.getBoolean(PreferenceHelper.PREF_KEY_MAPFRAGMENT_MODE_RECORDING, false)

    private fun removeLocationUpdates() {
        Log.i(LOG_TAG, "Removing location updates")
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            handlerThread.quit()
            stopSelf()
        } catch (unlikely: SecurityException) {
            Log.e(LOG_TAG, "Lost location permission. Could not remove updates. $unlikely")
        }
    }

    companion object {
        const val CHANNEL_DEFAULT_IMPORTANCE = "service_notification_channel"
        const val SERVICE_NOTIFICATION_ID = 1
        val LOG_TAG: String = LocationService::class.java.simpleName
    }
}