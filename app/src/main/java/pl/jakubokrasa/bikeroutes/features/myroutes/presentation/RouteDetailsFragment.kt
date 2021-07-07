package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_route_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.hideKeyboard
import pl.jakubokrasa.bikeroutes.core.extensions.makeGone
import pl.jakubokrasa.bikeroutes.core.extensions.makeVisible
import pl.jakubokrasa.bikeroutes.core.util.*
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.databinding.FragmentRouteDetailsBinding
import pl.jakubokrasa.bikeroutes.features.common.presentation.CommonRoutesNavigator
import pl.jakubokrasa.bikeroutes.features.common.presentation.PhotosRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable


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
        showRoute(view)
        initVisibilitySpinner()


        binding.btFollow.setOnClickListener(btFollowOnClick)
        binding.btAddPhotos.setOnClickListener(btAddPhotosOnClick)
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
        Log.e("TEST", "On Start")
        initPhotoRecycler()
        if(this::route.isInitialized)
            viewModel.getPhotos(route.routeId)
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyRecycler() // needed for every Recycler View Adapter while using Navigation Component
    }

    private fun destroyRecycler() {
        binding.rvPhotos.layoutManager = null
        binding.rvPhotos.adapter = null
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

    override fun initObservers() {
        super.initObservers()
        observePhotos()
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

    private fun hidePhotos() {
        binding.tvPhotos.makeGone()
        binding.rvPhotos.makeGone()
    }

    private fun showPhotos(list: List<PhotoInfoDisplayable>) {
        binding.tvPhotos.makeVisible()
        binding.rvPhotos.makeVisible()
        photosRecyclerAdapter.setItems(list)
    }

    private fun showRoute(view: View) {
        view.post {
            setRoute()
            setPoints()
            updateRouteInfoLayout()
            updateRouteEditLayout()
            setPolylineProperties()
            setMapViewProperties()
            binding.mapView.invalidate()

            if (!isMyRoute()) {
                binding.btAddPhotos.makeGone()
                if (route.sharingType == SharingType.PUBLIC_WITH_PRIVATE_PHOTOS) {
                    hidePhotos()
                }
            }
        }
    }

    private fun updateToolbar() {
        if (isMyRoute()) {
            binding.toolbar.inflateMenu(R.menu.menu_routedetails_home)
            binding.toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_routedetails_edit -> {
                        clearToolbarMenu()
                        binding.toolbar.inflateMenu(R.menu.menu_routedetails_edit)
                        binding.toolbar.setOnMenuItemClickListener(toolbarIconsEditModeOnClick)
                        binding.llRouteInfo.makeGone()
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
                with(binding) {
                    if(isRouteChanged()) {
                        updateRouteDisplayableModel()
                        viewModel.updateRoute(route)
                        updateRouteInfoLayout()
                    }

                    hideKeyboard()
                    llRouteEdit.makeGone()
                    llRouteInfo.makeVisible()
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
        (tvRouteName != etRouteName || tvRouteDescription != etRouteDescription || route.sharingType != spinnerVisibility.selectedItem as SharingType)

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

            if(route.description.isBlank()) tvRouteDescription.makeGone()
            else {
                tvRouteDescription.makeVisible()
                tvRouteDescription.text = route.description
            }

            when(route.sharingType) {
                SharingType.PUBLIC -> tvVisibility.text = "public"
                SharingType.PRIVATE -> tvVisibility.text = "only me"
                SharingType.PUBLIC_WITH_PRIVATE_PHOTOS -> tvVisibility.text = "private photos"
            }


            tvRouteDistance.text = getFormattedDistance(route.distance)
            tvRouteRideTime.text = getFormattedRideTime(route.rideTimeMinutes)

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
            ?.getParcelable<RouteDisplayable>(ROUTE_TO_FOLLOW_KEY)
            ?.let { route = it}
    }

    private fun setPoints() {
        val serializable = arguments
            ?.getSerializable(POINTS_TO_FOLLOW_KEY) //I use Serializable instead of Parcelable because I didn't find any simple solution to pass a List through Parcelable
                if(serializable is List<*>?) {
                    serializable.let {
                        points = serializable as List<PointDisplayable>
                    }
                }
    }

    private fun isMyRoute() = arguments?.getBoolean(IS_MY_ROUTE_KEY) ?: false

    private val btFollowOnClick = View.OnClickListener {
        navigator.openFollowRouteFragment(route, points)
    }

    private val btAddPhotosOnClick = View.OnClickListener {
        activityResultGalleryLauncher.launch("image/*")
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
        const val ROUTE_TO_FOLLOW_KEY = "routeToFollowKey"
        const val POINTS_TO_FOLLOW_KEY = "pointsToFollowKey"
        const val IS_MY_ROUTE_KEY = "isMyRoutesSourceKey"
    }
}