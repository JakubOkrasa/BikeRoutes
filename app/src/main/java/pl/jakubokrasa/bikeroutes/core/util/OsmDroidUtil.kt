package pl.jakubokrasa.bikeroutes.core.util

import android.content.Context
import android.graphics.Color
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

fun configureOsmDroid(context: Context) {
    Configuration.getInstance().load(context,
        PreferenceManager.getDefaultSharedPreferences(context))
}


fun MapView.setBaseMapViewProperties() {
    setTileSource(TileSourceFactory.WIKIMEDIA)
    zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
    isTilesScaledToDpi = true
    setMultiTouchControls(true)
}

fun Polyline.setBaseProperties() {
    if (!isEnabled) isEnabled = true //we get the location for the first time
    outlinePaint.strokeWidth = routeWidth
    outlinePaint.color = routeColor
}


const val routeColor = Color.BLUE
const val routeWidth = 7f