package pl.jakubokrasa.bikeroutes.core.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import androidx.preference.PreferenceManager
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.advancedpolyline.MonochromaticPaintList
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model.SegmentDisplayable

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

//fun Polyline.setBaseProperties() {
//    if (!isEnabled) isEnabled = true //we get the location for the first time
//    outlinePaint.strokeWidth = routeWidth
//    outlinePaint.color = routeColor
//}

fun getSegmentBorderPaint(): Paint {
    val paintBorder = Paint()
    paintBorder.strokeWidth = 20F
    paintBorder.style = Paint.Style.STROKE
    paintBorder.color = Color.BLACK
    paintBorder.strokeCap = Paint.Cap.ROUND
    paintBorder.isAntiAlias = true
    return paintBorder
}

fun addSegmentBorderPaint(segmentPolyline: Polyline) {
    val paintBorder = getSegmentBorderPaint()
    segmentPolyline.outlinePaintLists.add(MonochromaticPaintList(paintBorder))
}

@SuppressLint("ResourceType")
private fun getSegmentMappingPaint(
    segment: SegmentDisplayable, context: Context
): Paint {
    val paintMapping = Paint()
    paintMapping.isAntiAlias = true;
    paintMapping.strokeWidth = 15F;
    paintMapping.style = Paint.Style.FILL_AND_STROKE;
    paintMapping.strokeJoin = Paint.Join.ROUND;
    paintMapping.color = Color.RED
    if (segment.segmentColor.isNotEmpty()) paintMapping.color = Color.parseColor(segment.segmentColor)
    else paintMapping.color = Color.parseColor(context.resources.getString(R.color.seg_red))
    paintMapping.strokeCap = Paint.Cap.ROUND;
    paintMapping.isAntiAlias = true;
    return paintMapping
}

fun addSegmentMappingPaint(
    context: Context,
    segment: SegmentDisplayable,
    segmentPolyline: Polyline
) {
    val paintMapping = getSegmentMappingPaint(segment, context)
    segmentPolyline.outlinePaintLists.add(MonochromaticPaintList(paintMapping))
}

@SuppressLint("ResourceType")
private fun getMappingPaint(): Paint {
    val paintMapping = Paint()
    paintMapping.isAntiAlias = true;
    paintMapping.strokeWidth = 7F;
    paintMapping.style = Paint.Style.FILL_AND_STROKE;
    paintMapping.strokeJoin = Paint.Join.ROUND;
    paintMapping.color = Color.BLUE
    paintMapping.strokeCap = Paint.Cap.ROUND;
    paintMapping.isAntiAlias = true;
    return paintMapping
}

fun addMappingPaint(routePolyline: Polyline) {
    val paintMapping = getMappingPaint()
    routePolyline.outlinePaintLists.add(MonochromaticPaintList(paintMapping))
}


const val routeColor = Color.BLUE
const val routeWidth = 7f