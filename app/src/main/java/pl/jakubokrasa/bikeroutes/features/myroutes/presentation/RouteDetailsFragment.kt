package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.content.res.ColorStateList
import android.graphics.*
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.*
import pl.jakubokrasa.bikeroutes.core.util.*
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.databinding.FragmentRouteDetailsBinding
import pl.jakubokrasa.bikeroutes.features.common.presentation.CommonRoutesNavigator
import pl.jakubokrasa.bikeroutes.features.common.presentation.PhotosRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.common.segments.presentation.model.SegmentDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.reviews.presentation.ReviewsRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.reviews.presentation.model.ReviewDisplayable
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
    private val photosRecyclerAdapter: PhotosRecyclerAdapter by inject()
    private val reviewsRecyclerAdapter: ReviewsRecyclerAdapter by inject()
	private lateinit var segmentPolylines :ArrayList<Polyline>
    private lateinit var segments: ArrayList<SegmentDisplayable>
    private lateinit var reviews: ArrayList<ReviewDisplayable>
    private lateinit var selectedSegment: SegmentDisplayable
    private var zoom = -1.0
    private var currentUserReview: ReviewDisplayable? = null
    private val activityResultGalleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val picturePath = FileUtils(requireContext()).getPath(uri)
            viewModel.addPhoto(route.routeId, picturePath, route.sharingType)
//            initRecycler() //w MyRoutesFragment RV jest inicjowany także w onResume, nie pamiętam czemu
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentRouteDetailsBinding.bind(view)
        configureOsmDroid(requireContext())

        updateToolbar()
        onViewPost(view)
        initVisibilitySpinner()
        with(binding) {
            if (!isMyRoute()) {
                tvReviews.makeVisible()
                llAddReview.makeVisible()
            }
            btFollow.setOnClickListener(btFollowOnClick)
            ibRemoveSegment.setOnClickListener(ibRemoveSegmentOnClick)
            btSaveReview.setOnClickListener(btSaveReviewOnClick)
            btEditReview.setOnClickListener(btEditReviewOnClick)
            btUpdateReview.setOnClickListener(btUpdateReviewOnClick)
            btRemoveReview.setOnClickListener(btRemoveReviewOnClick)
            btCancelReviewEdit.setOnClickListener(btCancelReviewEditOnClick)
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

    override fun onStart() {
        super.onStart()
        initPhotoRecycler()
        initReviewRecycler()
        if(this::route.isInitialized)
            viewModel.getPhotos(route.routeId)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyRecyclers() // needed for every Recycler View Adapter while using Navigation Component
    }

    private fun destroyRecyclers() {
        binding.rvPhotos.layoutManager = null
        binding.rvReviews.layoutManager = null
        binding.rvPhotos.adapter = null
        binding.rvReviews.adapter = null
    }

    private fun initVisibilitySpinner() {
        val spinnerAdapter = ArrayAdapter(requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            SharingType.values())
        binding.spinnerVisibility.adapter = spinnerAdapter
    }

    private fun initPhotoRecycler() {
        with(binding.rvPhotos) {
            setHasFixedSize(true)
            adapter = photosRecyclerAdapter
            val photosLayoutManager = LinearLayoutManager(requireContext())
            photosLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = photosLayoutManager
            photosRecyclerAdapter.onItemClick = { photos, position -> navigator.openGalleryFragment(
                photos,
                position)
            }

        }
    }

    private fun initReviewRecycler() {
        with(binding.rvReviews) {
            setHasFixedSize(true)
            adapter = reviewsRecyclerAdapter
            val linearLayoutManager = LinearLayoutManager(requireContext())
            linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = linearLayoutManager
        }
    }

    override fun initObservers() {
        super.initObservers()
        observePhotos()
		observeSegments()
        observeShareUri()
        observeReviews()
    }

    private fun observePhotos() {
        viewModel.photos.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                showPhotos(it)
            } else {
                hidePhotos()
            }
        })
    }

    private fun observeShareUri() {
        viewModel.exportedRouteUri.observe(viewLifecycleOwner, { uri ->
                        Glide.with(requireContext())
//                .load(cardviewImage)
//                .placeholder(R.drawable.ic_baseline_photo_24)
//                .apply(RequestOptions().centerInside())
//                .into(binding.imgTestExport)


            startShareChooser(uri)

        })
    }

    private fun observeReviews() {
        viewModel.reviews.observe(viewLifecycleOwner, { list ->
            reviews = ArrayList(list)
            val otherUsersReviews = ArrayList<ReviewDisplayable>()
            currentUserReview = null
            list.forEach {
                if(it.userId == getCurrentUserUid(preferenceHelper))
                    currentUserReview = it
                else
                    otherUsersReviews.add(it)
            }
            showCurrentUserReview(currentUserReview)
            if(otherUsersReviews.isNotEmpty()) {
                showReviews(otherUsersReviews)
            } else {
                hideReviews()
            }
        })
    }

    private fun showReviews(reviews: List<ReviewDisplayable>) {
        reviewsRecyclerAdapter.setItems(reviews)
        if(reviewsRecyclerAdapter.itemCount>0) {
            binding.tvReviews.makeVisible()
            binding.rvReviews.makeVisible()
        }
    }

    private fun showCurrentUserReview(currentUserReview: ReviewDisplayable?) {
        if(currentUserReview!=null) {
            binding.tvCurrentReview.text = currentUserReview.content
            binding.llAddReview.makeGone()
            binding.llEditReview.makeVisible()
        } else if(!isMyRoute()) {
            binding.llEditReview.makeGone()
            binding.llAddReview.makeVisible()
        }
    }

    private fun hideReviews() {
        binding.tvReviews.makeGone()
        binding.rvReviews.makeGone()
    }

    private fun startShareChooser(uri: Uri) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.type = "image/png"
        startActivity(Intent.createChooser(intent, "Export route"))
    }

    private fun observeSegments() {
        viewModel.segments.observe(viewLifecycleOwner, {
            segments = ArrayList(it)
            showSegments(segments)
        })
    }

    private fun showSegments(segments: List<SegmentDisplayable>) {
        segmentPolylines = ArrayList(segments.size)
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

            segmentPolyline.setOnClickListener(segmentOnClickListener)
        }
        binding.mapView.invalidate()
    }

    private fun hidePhotos() {
        binding.tvPhotos.makeGone()
        binding.rvPhotos.makeGone()
    }

    private fun showPhotos(list: List<PhotoInfoDisplayable>) {
        binding.tvPhotos.makeVisible()
        binding.rvPhotos.makeVisible()
        photosRecyclerAdapter.setItems(list)
    }

    private fun onViewPost(view: View) {
        view.post {
            showRoute()
            viewModel.getPhotos(route.routeId)
            viewModel.getSegments(route.routeId)
            viewModel.getReviews(route.routeId)


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
		if (!isMyRoute()) {
                if (route.sharingType == SharingType.PUBLIC_WITH_PRIVATE_PHOTOS) {
                    hidePhotos()
                }
        }
    }

    private fun updateToolbar() {
        if (isMyRoute()) {
            binding.toolbar.inflateMenu(R.menu.menu_routedetails_home)
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_add_photo -> {
                        pickPhotoFromGallery()
                        true
                    }
                    R.id.action_segments -> {
                        navigator.openSegmentsFragment(route,
                            points,
                            segments)
                        true
                    }
                    R.id.action_share -> {
                        viewModel.exportRoute(route, polyline, segmentPolylines, zoom)
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

    private fun pickPhotoFromGallery() {
        activityResultGalleryLauncher.launch("image/*")
    }

    private val toolbarIconsEditModeOnClick = Toolbar.OnMenuItemClickListener { insideMenuItem ->
        when (insideMenuItem.itemId) {
            R.id.action_routedetails_done -> {
                with(binding) {
                    if(isRouteChanged()) {
                        updateRouteDisplayableModel()
                        viewModel.updateRoute(route)
                        updateRouteInfoLayout()
                    }

                    hideKeyboard()
                    llRouteEdit.makeGone()
                    llRouteInfo.makeVisible()
					binding.llVisibility.makeVisible()
                    clearToolbarMenu()
                    updateToolbar()
                    true
                }


            }
            R.id.action_routedetails_remove -> {
                if (isMyRoute()) initializeDialogRemove()
                dialogConfirmRemove.show()
                true
            }
            else -> false
        }
    }

    private fun FragmentRouteDetailsBinding.isRouteChanged() =
        (tvRouteName.text.toString() != etRouteName.text.toString()
        || tvRouteDescription.text.toString() != etRouteDescription.text.toString()
        || route.sharingType != spinnerVisibility.selectedItem as SharingType)


    private fun initializeDialogRemove() {
        dialogConfirmRemove = DialogConfirm(requireContext(),
            "Are you sure to remove this route?",
            "remove",
            viewModel,
            route)
    }

    private fun updateRouteDisplayableModel() {
        with(binding) {
            route.name = etRouteName.text.toString()
            route.description = etRouteDescription.text.toString()
//            if (swPrivate.isChecked) route.sharingType = SharingType.PRIVATE else route.sharingType = SharingType.PUBLIC
            when(spinnerVisibility.selectedItem) {
                SharingType.PUBLIC -> route.sharingType = SharingType.PUBLIC
                SharingType.PRIVATE -> route.sharingType = SharingType.PRIVATE
                SharingType.PUBLIC_WITH_PRIVATE_PHOTOS -> route.sharingType =
                    SharingType.PUBLIC_WITH_PRIVATE_PHOTOS
            }
        }
    }

    private fun updateRouteInfoLayout() {
        with(binding) {
            tvRouteName.text = route.name
            tvCreatedBy.text = route.createdBy.ifBlank { "no data" }

            if(route.description.isBlank()) tvRouteDescription.makeGone()
            else {
                tvRouteDescription.makeVisible()
                tvRouteDescription.text = route.description
            }

            tvRouteDistance.text = getFormattedDistance(route.distance)
            tvRouteRideTime.text = getFormattedRideTime(route.rideTimeMinutes)
            tvVisibility.text = getFormattedSharingTypeName(route.sharingType)

            if(isMyRoute())
                binding.llVisibility.makeVisible()
            else
                binding.llVisibility.makeGone()
        }
    }

    private fun updateRouteEditLayout() {
        with(binding) {
            etRouteName.setText(route.name)
            if (route.description.isNotBlank()) etRouteDescription.setText(route.description)

            when(route.sharingType) {
                SharingType.PUBLIC -> spinnerVisibility.setSelection(SharingType.PUBLIC.ordinal)
                SharingType.PRIVATE -> spinnerVisibility.setSelection(SharingType.PRIVATE.ordinal)
                SharingType.PUBLIC_WITH_PRIVATE_PHOTOS -> spinnerVisibility.setSelection(SharingType.PUBLIC_WITH_PRIVATE_PHOTOS.ordinal)
            }
        }
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
        zoom = zoomLevelDouble - 0.5
        controller.zoomTo(zoom)
    }

    private fun setPolylineProperties() {
        polyline = Polyline() // needed when you pop up from FollowRouteFragment to RouteDetailsFragment
        polyline.setPoints(points.map { p -> p.geoPoint })
        addMappingPaint(polyline)
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

    private val ibRemoveSegmentOnClick = View.OnClickListener {
        viewModel.removeSegment(selectedSegment.segmentId)
        val removedSegmentIndex = segments.indexOf(selectedSegment)
        binding.mapView.overlays.remove(segmentPolylines[removedSegmentIndex])
        segmentPolylines.removeAt(removedSegmentIndex)
        binding.mapView.invalidate()
        segments.removeAt(removedSegmentIndex)
        binding.llSegment.makeGone()
    }

    private val btSaveReviewOnClick = View.OnClickListener {
        if(binding.etAddReview.text.isNotEmpty()) {
            val review = ReviewDisplayable(
                "",
                getCurrentUserUid(preferenceHelper),
                getCurrentUserDisplayName(preferenceHelper),
                route.routeId,
                System.currentTimeMillis(),
                binding.etAddReview.text.toString())
            viewModel.addReview(review)
            binding.tvCurrentReview.text = review.content
            binding.llAddReview.makeGone()
            binding.llEditReview.makeVisible()
        } else
            showToast("Review is empty")
    }

    private val btEditReviewOnClick = View.OnClickListener {
        binding.etUpdateReview.setText(binding.tvCurrentReview.text.toString(), TextView.BufferType.EDITABLE)
        binding.llUpdateReview.makeVisible()
        binding.llEditReview.makeGone()
    }

    private val btUpdateReviewOnClick = View.OnClickListener {
        if (binding.etUpdateReview.text.isNotEmpty()) {
            currentUserReview!!.content = binding.etUpdateReview.text.toString()
            currentUserReview!!.createdAt = System.currentTimeMillis()
            viewModel.updateReview(currentUserReview!!)
            binding.tvCurrentReview.text = currentUserReview!!.content
            binding.llUpdateReview.makeGone()
            binding.llEditReview.makeVisible()
        } else
            showToast("Review is empty")
    }

    private val btRemoveReviewOnClick = View.OnClickListener {
        reviews.remove(currentUserReview!!)
        viewModel.removeReview(currentUserReview!!.reviewId)
        currentUserReview = null
        binding.llEditReview.makeGone()
        binding.etAddReview.text.clear()
        binding.etUpdateReview.text.clear()
        binding.llAddReview.makeVisible()
    }

    private val btCancelReviewEditOnClick = View.OnClickListener {
        binding.llUpdateReview.makeGone()
        binding.llEditReview.makeVisible()
    }

    private val segmentOnClickListener = object: Polyline.OnClickListener {
        override fun onClick(polyline: Polyline, mapView: MapView, eventPos: GeoPoint): Boolean {
            showSegmentDetails(polyline)
            return true
        }

        private fun showSegmentDetails(polyline: Polyline) {
            binding.llSegment.makeVisible()
            val segmentIndex = segmentPolylines.indexOf(polyline)
            selectedSegment = segments[segmentIndex]
            showSegmentType()
            showSegmentButtonsIfMyRoute()
            hideSegmentInfoIfEmpty(segmentIndex)
            setSegmentDetailsColor()
        }

        private fun showSegmentType() {
            binding.btSegmentType.text = selectedSegment.segmentType.toString().toUpperCase(Locale.ROOT)
        }

        private fun showSegmentButtonsIfMyRoute() {
            if (isMyRoute()) {
                binding.ibEdit.makeVisible()
                binding.ibRemoveSegment.makeVisible()
            }
        }

        private fun hideSegmentInfoIfEmpty(segmentIndex: Int) {
            if (selectedSegment.info.isNotEmpty()) {
                binding.llSegmentInfo.makeVisible()
                binding.tvSegmentInfo.text = segments[segmentIndex].info
            } else {
                binding.llSegmentInfo.makeGone()
            }
        }

        @SuppressLint("ResourceType")
        private fun setSegmentDetailsColor() {
            val segmentColor = selectedSegment.segmentColor.ifEmpty { requireContext().resources.getString(R.color.seg_red) }
            val colorStateList = ColorStateList.valueOf(Color.parseColor(segmentColor))
            binding.btSegmentType.backgroundTintList = colorStateList
            binding.ibEdit.backgroundTintList = colorStateList
            binding.ibRemoveSegment.backgroundTintList = colorStateList
        }
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

