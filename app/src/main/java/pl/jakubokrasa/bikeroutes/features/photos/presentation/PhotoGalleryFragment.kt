package pl.jakubokrasa.bikeroutes.features.photos.presentation

import android.os.Bundle
import android.view.View
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.FragmentPhotogalleryBinding
import pl.jakubokrasa.bikeroutes.features.photos.presentation.model.PhotoInfoDisplayable
import pl.jakubokrasa.bikeroutes.features.ui_features.myroutes.presentation.MyRoutesViewModel

class PhotoGalleryFragment(): BaseFragment<MyRoutesViewModel>(R.layout.fragment_photogallery) {
    override val viewModel: MyRoutesViewModel by sharedViewModel()
    private var _binding: FragmentPhotogalleryBinding? = null
    private val binding get() = _binding!!
    private var photos = ArrayList<PhotoInfoDisplayable>()
    private lateinit var galleryAdapter: PhotoGalleryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPhotogalleryBinding.bind(view)
        photos.addAll(getPhotos())

        galleryAdapter = PhotoGalleryAdapter(requireContext(), photos, viewModel)
        binding.viewPager.adapter = galleryAdapter
        binding.viewPager.setCurrentItem(getPosition(), false)
    }

    override fun initObservers() {
        super.initObservers()
        observePhotoRemovePos()
    }

    private fun observePhotoRemovePos() {
        viewModel.photoRemovePos.observe(viewLifecycleOwner, {
            if(photos.isNotEmpty())
                galleryAdapter.removePhoto(it)
        })
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

    private fun getPosition(): Int {
        return arguments?.getInt(PHOTO_POSITION_BUNDLE_KEY)?: 0
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyRecycler() // needed for every Recycler View Adapter while using Navigation Component
    }

    private fun destroyRecycler() {
//      without "binding.viewPager.layoutManager = null" (in viewpager is no layoutManager)
        binding.viewPager.adapter = null
    }

    companion object {
        const val PHOTOS_BUNDLE_KEY = "photosBundleKey"
        const val PHOTO_POSITION_BUNDLE_KEY = "photoPositionBundleKey"
    }
}