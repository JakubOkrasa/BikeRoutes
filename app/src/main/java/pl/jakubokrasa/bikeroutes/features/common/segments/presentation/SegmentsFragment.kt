package pl.jakubokrasa.bikeroutes.features.common.segments.presentation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.java.KoinJavaComponent.inject
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.core.util.*
import pl.jakubokrasa.bikeroutes.databinding.FragmentSegmentsBinding
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.DialogSegment
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment.Companion.POINTS_BUNDLE_KEY
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment.Companion.ROUTE_BUNDLE_KEY


class SegmentsFragment: BaseFragment<MyRoutesViewModel>(R.layout.fragment_segments) {

    override val viewModel: MyRoutesViewModel by sharedViewModel()
    private var _binding: FragmentSegmentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var route: RouteDisplayable
    private lateinit var points: List<PointDisplayable>
    private val polyline = Polyline()

    private lateinit var markerBegin: Marker
    private lateinit var markerEnd: Marker
    private var stage = CreateSegmentStage.CHOOSE_BEGIN
    private var segmentBeginIndex = -1
    private var segmentEndIndex = -1
    private lateinit var segmentPolyline: Polyline

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSegmentsBinding.bind(view)
        configureOsmDroid(requireContext())

        showRoute(view)
        binding.btRecenter.setOnClickListener(btRecenterOnClick)


        initMarkerBegin()
        initMarkerEnd()

        stage = CreateSegmentStage.CHOOSE_BEGIN
        binding.mapView.overlays.add(mapTapEvents)
    }

    private fun initMarkerBegin() {
        markerBegin = Marker(binding.mapView)
        markerBegin.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        markerBegin.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_location_green, null)
    }

    private fun initMarkerEnd() {
        markerEnd = Marker(binding.mapView)
        markerEnd.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        markerEnd.icon =
            ResourcesCompat.getDrawable(resources, R.drawable.ic_location_green, null)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume();
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun initObservers() {
        super.initObservers()
        observeSegmentPointIndex()
    }

    private fun observeSegmentPointIndex() {
        viewModel.segmentPointIndex.observe(viewLifecycleOwner, {
            if(stage == CreateSegmentStage.CHOOSE_BEGIN) {
                segmentBeginIndex = it
                showSegmentPoint(it, markerBegin)
                stage = CreateSegmentStage.CHOOSE_END
            } else if(stage == CreateSegmentStage.CHOOSE_END) {
                segmentEndIndex = it
                showSegmentPoint(it, markerEnd)
                showSegmentPolyline()
                stage = CreateSegmentStage.NONE
                updateToolbar()
            }
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
        binding.toolbar.inflateMenu(R.menu.menu_segments_home)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_save_segment -> {
                    DialogSegment(requireContext(), viewModel).show()
                    clearToolbarMenu()
//                    binding.toolbar.inflateMenu(R.menu.menu_segments_add)
//                    binding.toolbar.setOnMenuItemClickListener(toolbarIconsAddModeOnClick)
                    true
                }
                else -> false
            }
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
        if(segmentBeginIndex<segmentEndIndex) {
            segmentPoints = points.subList(segmentBeginIndex, segmentEndIndex + 1).map { it.geoPoint }
        } else {
            segmentPoints = points.subList(segmentEndIndex, segmentBeginIndex + 1).map { it.geoPoint }
        }
        segmentPolyline.setPoints(segmentPoints)
    }

    private val toolbarIconsAddModeOnClick = Toolbar.OnMenuItemClickListener { markBeginMenuItem ->
        when (markBeginMenuItem.itemId) {
            R.id.action_begin_marked -> {


                clearToolbarMenu()
                updateToolbar()
                true
            }
            else -> false
        }
    }

    private fun showRoute(view: View) {
        view.post {
            setRoute()
            setPoints()
            setPolylineProperties()
            setMapViewProperties()
            binding.mapView.invalidate()
        }
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
        NONE,
        CHOOSE_BEGIN,
        CHOOSE_END
    }

}