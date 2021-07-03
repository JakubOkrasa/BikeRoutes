package pl.jakubokrasa.bikeroutes.features.common.presentation

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.FragmentPhotogalleryBinding
import pl.jakubokrasa.bikeroutes.features.common.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.PointDisplayable
import pl.jakubokrasa.bikeroutes.features.map.presentation.model.RouteDisplayable
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.MyRoutesViewModel
import pl.jakubokrasa.bikeroutes.features.myroutes.presentation.RouteDetailsFragment

class PhotoGalleryFragment(): BaseFragment<MyRoutesViewModel>(R.layout.fragment_photogallery) {
    override val viewModel: MyRoutesViewModel by sharedViewModel()
    private var _binding: FragmentPhotogalleryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPhotogalleryBinding.bind(view)

        val galleryAdapter = PhotoGalleryAdapter(requireContext(), getPhotos())
        binding.viewPager.adapter = galleryAdapter

    }

    private fun getPhotos(): List<PhotoInfoDisplayable> {
        val serializable =
            arguments?.getSerializable(PHOTOS_BUNDLE_KEY) as List<PhotoInfoDisplayable>
        if (serializable is List<*>?) {
            serializable.let {
                return serializable
            }
        }
        return emptyList()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyRecycler() // needed for every Recycler View Adapter while using Navigation Component
    }

    private fun destroyRecycler() {
//        binding.viewPager.layoutManager = null
        binding.viewPager.adapter = null
    }

    companion object {
        const val PHOTOS_BUNDLE_KEY = "photosBundleKey"
        const val PHOTO_POSITION_BUNDLE_KEY = "photoPositionBundleKey"
    }
}