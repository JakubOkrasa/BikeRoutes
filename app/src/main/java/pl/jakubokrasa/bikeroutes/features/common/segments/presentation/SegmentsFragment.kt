package pl.jakubokrasa.bikeroutes.features.common.segments.presentation

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.edit
import androidx.core.content.res.ResourcesCompat
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_TIPS_SEGMENTS_NOT_SHOWED
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.core.util.*
import pl.jakubokrasa.bikeroutes.databinding.FragmentSegmentsBinding
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model.SegmentDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.DialogSegment
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment.Companion.POINTS_BUNDLE_KEY
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment.Companion.ROUTE_BUNDLE_KEY
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment.Companion.SEGMENTS_BUNDLE_KEY


class SegmentsFragment: BaseFragment<MyRoutesViewModel>(R.layout.fragment_segments) {

    override val viewModel: MyRoutesViewModel by sharedViewModel()
    private var _binding: FragmentSegmentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var route: RouteDisplayable
    private lateinit var points: List<PointDisplayable>
    private val polyline = Polyline()

    private lateinit var markerBegin: Marker
    private lateinit var markerEnd: Marker
    private lateinit var stage: CreateSegmentStage
    private var segmentBeginIndex = -1
    private var segmentEndIndex = -1
    private lateinit var segmentPolyline: Polyline
    private lateinit var segments: List<SegmentDisplayable>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSegmentsBinding.bind(view)
        configureOsmDroid(requireContext())

        showRoute(view)
        binding.btRecenter.setOnClickListener(btRecenterOnClick)
        binding.btAddSegment.setOnClickListener(btAddOnClick)
        binding.btNewSegment.setOnClickListener(btNewOnClick)
        setToolbarText("Segments")

        initMarkerBegin()
        initMarkerEnd()

        stage = CreateSegmentStage.SHOW_CURRENT_SEGMENTS
        binding.mapView.overlays.add(mapTapEvents)

        showSegmentsTipDialogForFirstTimeUsage()
    }



    private fun initMarkerBegin() {
        markerBegin = Marker(binding.mapView)
        markerBegin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        markerBegin.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_location_green, null)
        markerBegin.setInfoWindow(null)
    }

    private fun initMarkerEnd() {
        markerEnd = Marker(binding.mapView)
        markerEnd.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        markerEnd.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_location_green, null)
        markerEnd.setInfoWindow(null)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume();
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    private fun showSegmentsTipDialogForFirstTimeUsage() {
        if(!preferenceHelper.preferences.contains(PREF_KEY_TIPS_SEGMENTS_NOT_SHOWED))
            AlertDialog.Builder(requireContext())
                .setTitle("Tip")
                .setMessage("Here you can mark different route segments such as bumpy, sandy or busy road. To do that, tap \"New Segment\" button an then on the begin and the end point of the segment")
                .setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                    preferenceHelper.preferences.edit {
                        putBoolean(PREF_KEY_TIPS_SEGMENTS_NOT_SHOWED, false)
                    }
                }).show()
    }

    override fun initObservers() {
        super.initObservers()
        observeSegmentPointIndex()
        observeIsSegmentAdded()
        observeSegments()
    }

    private fun observeSegmentPointIndex() {
        viewModel.segmentPointIndex.observe(viewLifecycleOwner, {
            if(stage == CreateSegmentStage.CHOOSE_BEGIN) {
                segmentBeginIndex = it
                showSegmentPoint(it, markerBegin)
                setToolbarText(getString(R.string.fragment_segments_toolbarend))
                stage = CreateSegmentStage.CHOOSE_END
                binding.btNewSegment.makeGone()
            } else if(stage == CreateSegmentStage.CHOOSE_END) {
                segmentEndIndex = it
                showSegmentPoint(it, markerEnd)
                showSegmentPolyline()
                binding.btAddSegment.makeVisible()
                setToolbarText(getString(R.string.fragment_segments_toolbarready))
                stage = CreateSegmentStage.READY_TO_SAVE
            }
            updateToolbar()
        })
    }

    private fun observeIsSegmentAdded() {
        viewModel.isSegmentAdded.observe(viewLifecycleOwner, { success ->
            prepareToAddTheNextSegment() //onSuccess and onFailure
            if(success)
                viewModel.getSegments(route.routeId)
        })
    }

    private fun observeSegments() {
        viewModel.segments.observe(viewLifecycleOwner, { newSegments ->

            //todo possible optimization (by checking which segment already exists)
            segments = newSegments
            showSegments()
        })
    }

    private fun showSegmentPoint(index: Int?, marker: Marker) {
        index?.let {
            marker.position = points[it].geoPoint
            binding.mapView.overlays.add(marker)
            binding.mapView.invalidate()
        }
    }

    private fun updateToolbar() {
        if(stage == CreateSegmentStage.CHOOSE_END) {
            binding.toolbar.inflateMenu(R.menu.menu_segments_undo)
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_undo -> {
                        if(stage == CreateSegmentStage.CHOOSE_END) {
                            segmentBeginIndex = -1
                            binding.mapView.overlays.remove(markerBegin)
                            binding.mapView.invalidate()
                            binding.btAddSegment.makeGone()
                            setToolbarText(getString(R.string.fragment_segments_toolbarbegin))
                            clearToolbarMenu()
                            stage = CreateSegmentStage.CHOOSE_BEGIN
                        } else if(stage == CreateSegmentStage.READY_TO_SAVE) {
                            segmentEndIndex = -1
                            binding.mapView.overlays.remove(markerEnd)
                            binding.mapView.overlays.remove(segmentPolyline)
                            binding.mapView.invalidate()
                            binding.btAddSegment.makeGone()
                            stage = CreateSegmentStage.CHOOSE_END
                            setToolbarText(getString(R.string.fragment_segments_toolbarend))
                        }

                        true
                    }
                    else -> false
                }
            }
        } else {

        }

    }

    private fun showSegmentPolyline() {
        segmentPolyline = Polyline()
        setSegmentPoints()
        segmentPolyline.outlinePaint.strokeWidth = routeWidth
        segmentPolyline.outlinePaint.color = Color.RED
        binding.mapView.overlayManager.add(segmentPolyline)
        binding.mapView.invalidate()
    }

    private fun setSegmentPoints() {
        val segmentPoints: List<GeoPoint>
        Log.d("TEST", String.format("points.size=${points.size} beginIndex=$segmentBeginIndex endIndex=$segmentEndIndex"))
        if(segmentBeginIndex<segmentEndIndex) {
            segmentPoints = points.subList(segmentBeginIndex, segmentEndIndex + 1).map { it.geoPoint } // +1 since sublist's second index is exclusive
        } else {
            segmentPoints = points.subList(segmentEndIndex, segmentBeginIndex - 1).map { it.geoPoint }
        }
        segmentPolyline.setPoints(segmentPoints)
    }

    private fun showRoute(view: View) {
        view.post {
            setRoute()
            setPoints()
            setPolylineProperties()
            setMapViewProperties()
            setSegments()
            showSegments()
            binding.mapView.invalidate()
        }
    }

    private fun showSegments() {
        val segmentPolylines = ArrayList<Polyline>(segments.size)
        for(segment in segments) {
            val segmentPolyline = Polyline()
            addSegmentBorderPaint(segmentPolyline)
            addSegmentMappingPaint(requireContext(), segment, segmentPolyline)

            val segmentPoints: List<GeoPoint>
            if(segment.beginIndex<segment.endIndex) {
                segmentPoints = points.subList(segment.beginIndex, segment.endIndex + 1).map { it.geoPoint }
            } else {
                segmentPoints = points.subList(segment.endIndex, segment.beginIndex - 1).map { it.geoPoint }
            }
            segmentPolyline.setPoints(segmentPoints)
            segmentPolylines.add(segmentPolyline)
            binding.mapView.overlays.add(segmentPolyline)

        }
        binding.mapView.invalidate()
    }

    private fun setMapViewProperties() {
        with(binding.mapView) {
            setBaseMapViewProperties()
            overlayManager.add(polyline)
            maxZoomLevel = 16.0 // Because begin and the end of the segment can be only where a Geopoint exists in a route
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

    @SuppressLint("ClickableViewAccessibility")
    private val btRecenterOnClick = View.OnClickListener {
        binding.mapView.recenterRouteWithAnimation()
    }

    private val btAddOnClick = View.OnClickListener {
        val segmentBasicModel = SegmentBasicModel(
            route.routeId,
            segmentBeginIndex,
            segmentEndIndex,
        )
        DialogSegment(requireContext(), viewModel, segmentBasicModel).show()
    }

    private val btNewOnClick = View.OnClickListener {
        stage = CreateSegmentStage.CHOOSE_BEGIN
        binding.btNewSegment.makeGone()
        setToolbarText(getString(R.string.fragment_segments_toolbarbegin))
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

    private fun setSegments() {
        val serializable = arguments
            ?.getSerializable(SEGMENTS_BUNDLE_KEY) //I use Serializable instead of Parcelable because I didn't find any simple solution to pass a List through Parcelable
        if(serializable is List<*>?) {
            serializable.let {
                segments = serializable as List<SegmentDisplayable>
            }
        }
    }

    private fun prepareToAddTheNextSegment() {
        clearToolbarMenu()
        setToolbarText("Add another segment")
        binding.btAddSegment.makeGone()
        binding.btNewSegment.makeVisible()
        stage = CreateSegmentStage.SHOW_CURRENT_SEGMENTS
        segmentBeginIndex = -1
        segmentEndIndex = -1
        markerBegin.remove(binding.mapView)
        markerEnd.remove(binding.mapView)
        binding.mapView.overlays.remove(segmentPolyline)
        binding.mapView.invalidate()
    }

    private fun setToolbarText(text: String) {
        binding.tvToolbarTitle.text = text
    }

    private fun clearToolbarMenu() {
        binding.toolbar.menu.clear()
    }

    override fun onPendingState() {
        super.onPendingState()
        binding.progressLayout.makeVisible()
    }

    override fun onIdleState() {
        super.onIdleState()
        binding.progressLayout.makeGone()
    }



    private val mapTapEventsReceiver = object: MapEventsReceiver {
        override fun singleTapConfirmedHelper(geoPoint: GeoPoint?): Boolean {
            geoPoint?.let {
                if(stage == CreateSegmentStage.CHOOSE_BEGIN || stage == CreateSegmentStage.CHOOSE_END)
                    viewModel.getSegmentPoint(geoPoint, points, binding.mapView.zoomLevelDouble)

            }
            return false
        }
        override fun longPressHelper(p: GeoPoint?) = false
    }

    private val mapTapEvents = MapEventsOverlay(mapTapEventsReceiver)

    private enum class CreateSegmentStage {
        SHOW_CURRENT_SEGMENTS,
        CHOOSE_BEGIN,
        CHOOSE_END,
        READY_TO_SAVE
    }

}

data class SegmentBasicModel(
    val routeId: String,
    val beginIndex: Int,
    val endIndex: Int
)