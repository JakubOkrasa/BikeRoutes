package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.advancedpolyline.MonochromaticPaintList
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.hideKeyboard
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.core.util.*
import pl.jakubokrasa.bikeroutes.core.util.enums.sharingType
import pl.jakubokrasa.bikeroutes.databinding.FragmentRouteDetailsBinding
import pl.jakubokrasa.bikeroutes.features.common.presentation.CommonRoutesNavigator
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model.SegmentDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import java.util.*
import kotlin.collections.ArrayList


class RouteDetailsFragment : BaseFragment<MyRoutesViewModel>(R.layout.fragment_route_details) {

    override val viewModel: MyRoutesViewModel by sharedViewModel()
    private var _binding: FragmentRouteDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var route: RouteDisplayable
    private lateinit var points: List<PointDisplayable>
    private lateinit var polyline: Polyline
    private lateinit var dialogConfirmRemove: Dialog
    private val navigator: CommonRoutesNavigator by inject()
    private lateinit var segmentPolylines :ArrayList<Polyline>
    private lateinit var segments: List<SegmentDisplayable>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRouteDetailsBinding.bind(view)
        configureOsmDroid(requireContext())

        updateToolbar()
        onViewPost(view)

        binding.btFollow.setOnClickListener(btFollowOnClick)

    }

    override fun initObservers() {
        super.initObservers()
        observeSegments()
    }

    private fun observeSegments() {
        viewModel.segments.observe(viewLifecycleOwner, {
            segments = it
            showSegments(segments)
        })
    }

    private fun showSegments(segments: List<SegmentDisplayable>) {
        segmentPolylines = ArrayList(segments.size)
        for(segment in segments) {
            val segmentPolyline = Polyline()
            addBorderPaint(segmentPolyline)
            addMappingPaint(segment, segmentPolyline)

            val segmentPoints: List<GeoPoint>
            if(segment.beginIndex<segment.endIndex) {
                segmentPoints = points.subList(segment.beginIndex, segment.endIndex + 1).map { it.geoPoint }
            } else {
                segmentPoints = points.subList(segment.endIndex, segment.beginIndex - 1).map { it.geoPoint }
            }
            segmentPolyline.setPoints(segmentPoints)
            segmentPolylines.add(segmentPolyline)
            binding.mapView.overlays.add(segmentPolyline)

            segmentPolyline.setOnClickListener(segmentOnClickListener)
        }
        binding.mapView.invalidate()
    }

    @SuppressLint("ResourceType")
    private fun addMappingPaint(
        segment: SegmentDisplayable, segmentPolyline: Polyline
    ) {
        val paintMapping = Paint()
        paintMapping.isAntiAlias = true;
        paintMapping.strokeWidth = 15F;
        paintMapping.style = Paint.Style.FILL_AND_STROKE;
        paintMapping.strokeJoin = Paint.Join.ROUND;
        paintMapping.color = Color.RED
        if(segment.segmentColor.isNotEmpty())
            paintMapping.color = Color.parseColor(segment.segmentColor)
        else
            paintMapping.color = Color.parseColor(requireContext().resources.getString(R.color.seg_red))
        paintMapping.strokeCap = Paint.Cap.ROUND;
        paintMapping.isAntiAlias = true;
        segmentPolyline.outlinePaintLists.add(MonochromaticPaintList(paintMapping))
    }

    private fun addBorderPaint(segmentPolyline: Polyline) {
        val paintBorder = Paint()
        paintBorder.strokeWidth = 20F
        paintBorder.style = Paint.Style.STROKE
        paintBorder.color = Color.BLACK
        paintBorder.strokeCap = Paint.Cap.ROUND
        paintBorder.isAntiAlias = true
        segmentPolyline.outlinePaintLists.add(MonochromaticPaintList(paintBorder))
    }

    private val segmentOnClickListener = object: Polyline.OnClickListener {
        override fun onClick(polyline: Polyline, mapView: MapView, eventPos: GeoPoint): Boolean {
            binding.llSegment.makeVisible()
            val segmentIndex = segmentPolylines.indexOf(polyline)
            binding.btSegmentType.text =
                segments[segmentIndex].segmentType.toString().toUpperCase(Locale.ROOT)
            if(segments[segmentIndex].info.isNotEmpty()) {
                binding.llSegmentInfo.makeVisible()
                binding.tvSegmentInfo.text = segments[segmentIndex].info
                binding
            } else {
                binding.llSegmentInfo.makeGone()
            }
            return true
        }
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume();
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    private fun onViewPost(view: View) {
        view.post {
            showRoute()
            viewModel.getSegments(route.routeId)
        }
    }

    private fun showRoute() {
        setRoute()
        setPoints()
        updateRouteInfoLayout()
        updateRouteEditLayout()
        setPolylineProperties()
        setMapViewProperties()
        binding.mapView.invalidate()
    }

    private fun updateToolbar() {
        if (isMyRoute()) {
            binding.toolbar.inflateMenu(R.menu.menu_routedetails_home)
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_segments -> {
                        navigator.openSegmentsFragment(route,
                            points,
                            ArrayList<SegmentDisplayable>()) //todo
                        true
                    }
                    R.id.action_routedetails_edit -> {
                        clearToolbarMenu()
                        binding.toolbar.inflateMenu(R.menu.menu_routedetails_edit)
                        binding.toolbar.setOnMenuItemClickListener(toolbarIconsEditModeOnClick)
                        binding.llRouteInfo.makeGone()
                        binding.llVisibility.makeGone()
                        binding.llRouteEdit.makeVisible()
                        true
                    }
                    else -> false
                }
            }
        }
    }

	private val toolbarIconsEditModeOnClick = Toolbar.OnMenuItemClickListener { insideMenuItem ->
        when (insideMenuItem.itemId) {
            R.id.action_routedetails_done -> {
                updateRouteDisplayableModel()
                viewModel.updateRoute(route)
                updateRouteInfoLayout()
                hideKeyboard()
                binding.llRouteEdit.makeGone()
                binding.llRouteInfo.makeVisible()
                binding.llVisibility.makeVisible()
                clearToolbarMenu()
                updateToolbar()
                true
            }
            R.id.action_routedetails_remove -> {
                if (isMyRoute()) initializeDialogRemove()
                dialogConfirmRemove.show()
                true
            }
            else -> false
        }
    }

    private fun initializeDialogRemove() {
        dialogConfirmRemove = DialogConfirm(requireContext(),
            "Are you sure to remove this route?",
            "remove",
            viewModel,
            route)
    }

    private fun updateRouteDisplayableModel() {
        route.name = binding.etRouteName.text.toString()
        route.description = binding.etRouteDescription.text.toString()
        if (binding.swPrivate.isChecked) route.sharingType = sharingType.PRIVATE else route.sharingType = sharingType.PUBLIC
    }

    private fun updateRouteInfoLayout() {
        with(binding) {
            tvRouteName.text = route.name

            if(route.description.isBlank()) tvRouteDescription.makeGone()
            else {
                tvRouteDescription.makeVisible()
                tvRouteDescription.text = route.description
            }

            if(route.sharingType == sharingType.PUBLIC)
                tvVisibility.text = "public"
            else if(route.sharingType == sharingType.PRIVATE)
                tvVisibility.text = "only me"


            tvRouteDistance.text = getFormattedDistance(route.distance)
            tvRouteRideTime.text = getFormattedRideTime(route.rideTimeMinutes)

            if(isMyRoute())
                binding.llVisibility.makeVisible()
            else
                binding.llVisibility.makeGone()
        }
    }

    private fun updateRouteEditLayout() {
        binding.etRouteName.setText(route.name)
        if(route.description.isNotBlank()) binding.etRouteDescription.setText(route.description)
        binding.swPrivate.isChecked = run { route.sharingType == sharingType.PRIVATE }
    }

    private fun setMapViewProperties() {
        with(binding.mapView) {
            setBaseMapViewProperties()
            overlayManager.add(polyline)
            recenterRoute()
        }
    }

    private fun MapView.recenterRoute() {
        zoomToBoundingBox(polyline.bounds, false, 18, 18.0, 0)
        val currentZoom = zoomLevelDouble
        controller.zoomTo(currentZoom - 0.5)
    }

    private fun setPolylineProperties() {
        polyline = Polyline() // needed when you pop up from FollowRouteFragment to RouteDetailsFragment
        polyline.setPoints(points.map { p -> p.geoPoint })
        polyline.setBaseProperties()
    }

    private fun clearToolbarMenu() {
        binding.toolbar.menu.clear()
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

    private fun isMyRoute() = arguments?.getBoolean(IS_MY_ROUTE_BUNDLE_KEY) ?: false

    private val btFollowOnClick = View.OnClickListener {
        navigator.openFollowRouteFragment(route, points)
    }

    override fun onPendingState() {
        super.onPendingState()
        binding.progressLayout.makeVisible()
    }

    override fun onIdleState() {
        super.onIdleState()
        binding.progressLayout.makeGone()
    }

    companion object {
        const val ROUTE_BUNDLE_KEY = "routeBundleKey"
        const val POINTS_BUNDLE_KEY = "pointsBundleKey"
        const val SEGMENTS_BUNDLE_KEY = "segmentsBundleKey"
        const val IS_MY_ROUTE_BUNDLE_KEY = "isMyRouteBundleKey"
    }
}