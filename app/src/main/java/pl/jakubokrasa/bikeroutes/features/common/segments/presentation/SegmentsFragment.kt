package pl.jakubokrasa.bikeroutes.features.common.segments.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.slider.Slider
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
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
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment.Companion.POINTS_BUNDLE_KEY
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment.Companion.ROUTE_BUNDLE_KEY
import kotlin.math.roundToInt


class SegmentsFragment: BaseFragment<MyRoutesViewModel>(R.layout.fragment_segments) {

    override val viewModel: MyRoutesViewModel by sharedViewModel()
    private var _binding: FragmentSegmentsBinding? = null
    private val binding get() = _binding!!
    private lateinit var route: RouteDisplayable
    private lateinit var points: List<PointDisplayable>
    private val polyline = Polyline()

    private var thresholdDistance = 100
    private lateinit var marker: Marker

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSegmentsBinding.bind(view)
        configureOsmDroid(requireContext())

        showRoute(view)
        binding.btRecenter.setOnClickListener(btRecenterOnClick)

        updateToolbar()


        val overlayEvents = MapEventsOverlay(mapEventsReceiver)
        binding.mapView.overlays.add(overlayEvents)

        //slider
        binding.sliderTest.value = thresholdDistance.toFloat()
        binding.sliderTest.valueFrom = 0.0f
        binding.sliderTest.valueTo = 160.0f
        binding.sliderTest.addOnChangeListener(Slider.OnChangeListener { _, _, _ ->
            thresholdDistance = binding.sliderTest.value.toInt()
        })

        binding.btUpdate.setOnClickListener {
            binding.tvZoom.text = String.format("%.2f", binding.mapView.zoomLevelDouble)
        }

        marker = Marker(binding.mapView)

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
        observeSegmentBeginIndex()
    }

    private fun observeSegmentBeginIndex() {
        viewModel.segmentBeginIndex.observe(viewLifecycleOwner, {
            showBeginIndex(it)
        })
    }

    private fun showBeginIndex(index: Int?) {
        index?.let {
            marker.position = points[it].geoPoint
            marker.icon = ResourcesCompat.getDrawable(resources,
                R.drawable.location_marker,
                null)
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            binding.mapView.overlays.add(marker)
            binding.mapView.invalidate()
        }
    }

    private fun updateToolbar() {
        binding.toolbar.inflateMenu(R.menu.menu_segments_home)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_add_segment -> {
                    clearToolbarMenu()
                    binding.toolbar.inflateMenu(R.menu.menu_segments_add)
                    binding.toolbar.setOnMenuItemClickListener(toolbarIconsAddModeOnClick)
                    true
                }
                else -> false
            }
        }
    }

    private val toolbarIconsAddModeOnClick = Toolbar.OnMenuItemClickListener { markBeginMenuItem ->
        when (markBeginMenuItem.itemId) {
            R.id.action_begin_marked -> {
                //todo

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

    private val mapEventsReceiver = object: MapEventsReceiver {
        override fun singleTapConfirmedHelper(geoPoint: GeoPoint?): Boolean {
            geoPoint?.let { viewModel.getSegmentBegin(geoPoint, points, thresholdDistance) }
            return false
        }
        override fun longPressHelper(p: GeoPoint?) = false
    }
}