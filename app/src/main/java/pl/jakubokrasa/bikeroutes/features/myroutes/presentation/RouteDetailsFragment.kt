package pl.jakubokrasa.bikeroutes.features.myroutes.presentation

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
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
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.core.util.FileUtils
import pl.jakubokrasa.bikeroutes.features.common.presentation.PhotosRecyclerAdapter
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable


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
        initRecycler()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyRecycler() // needed for every Recycler View Adapter while using Navigation Component
    }

    private fun destroyRecycler() {
        binding.rvPhotos.layoutManager = null
        binding.rvPhotos.adapter = null
    }

    private fun initRecycler() {
        with(binding.rvPhotos) {
            setHasFixedSize(true)
            adapter = photosRecyclerAdapter
            val photosLayoutManager = LinearLayoutManager(requireContext())
            photosLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
            layoutManager = photosLayoutManager
//            photosRecyclerAdapter.onItemClick = {
//                    photo ->
//            }

        }
    }

    override fun initObservers() {
        super.initObservers()
        observePhotos()
    }

    private fun observePhotos() {
        viewModel.photos.observe(viewLifecycleOwner, {
            if(it.isNotEmpty()) {
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
//        binding.tvNoData.makeGone()
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
//                        binding.llVisibility.makeGone()
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
//                binding.llVisibility.makeVisible()
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
        if (binding.swPrivate.isChecked) route.sharingType = SharingType.PRIVATE else route.sharingType = SharingType.PUBLIC
    }

    private fun updateRouteInfoLayout() {
        with(binding) {
            tvRouteName.text = route.name

            if(route.description.isBlank()) tvRouteDescription.makeGone()
            else {
                tvRouteDescription.makeVisible()
                tvRouteDescription.text = route.description
            }

            if(route.sharingType == SharingType.PUBLIC)
                tvVisibility.text = "public"
            else if(route.sharingType == SharingType.PRIVATE)
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
        binding.swPrivate.isChecked = run { route.sharingType == SharingType.PRIVATE }
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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK && requestCode == <unique_code>) {
//            imageView.setImageBitmap(getPicture(data.getData()));
//        }
//    }
//
//    public static Bitmap getPicture(Uri selectedImage) {
//        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//        Cursor cursor = getContext().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
//        cursor.moveToFirst();
//        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//        String picturePath = cursor.getString(columnIndex);
//        cursor.close();
//        return BitmapFactory.decodeFile(picturePath);
//    }

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