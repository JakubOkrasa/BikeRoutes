package pl.jakubokrasa.bikeroutes.features.ui_features.map.presentation

import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.presentation.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.hideKeyboard
import pl.jakubokrasa.bikeroutes.core.util.PreferenceHelper.Companion.PREF_KEY_DISTANCE_SUM
import pl.jakubokrasa.bikeroutes.core.util.PreferenceHelper.Companion.PREF_KEY_USER_DISPLAY_NAME
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.databinding.FragmentSaveRouteBinding
import pl.jakubokrasa.bikeroutes.features.filter.domain.model.BoundingBoxData
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.DataSaveRoute
import pl.jakubokrasa.bikeroutes.features.ui_features.map.navigation.MapFrgNavigator

class SaveRouteFragment : BaseFragment<MapViewModel>(R.layout.fragment_save_route) {

    private var _binding: FragmentSaveRouteBinding? = null
    private val binding get() = _binding!!
    private val mapFrgNavigator: MapFrgNavigator by inject()
    override val viewModel: MapViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSaveRouteBinding.bind(view)
        binding.btSave.setOnClickListener(btSaveOnClick)
        binding.btCancel.setOnClickListener(btCancelOnClick)
    }

    private val btSaveOnClick = View.OnClickListener() {
        val name = binding.etName.text.toString()
        if(name.isEmpty())
            showToast("Route must have a name")
        else  {
            val description = binding.etDescription.text.toString()
            val distance = preferenceHelper.preferences.getInt(PREF_KEY_DISTANCE_SUM, 0)
            val sharingType = if (binding.swPrivate.isChecked) SharingType.PRIVATE else SharingType.PUBLIC
            val createdBy = preferenceHelper.preferences.getString(PREF_KEY_USER_DISPLAY_NAME, "unknown-user")
            viewModel.saveRoute(DataSaveRoute(name, description, distance, sharingType, getBoundingBoxData(), createdBy!!))
            hideKeyboard()
            mapFrgNavigator.goBack()
        }
    }

    private val btCancelOnClick = View.OnClickListener {
        mapFrgNavigator.goBack()
    }

    private fun getBoundingBoxData(): BoundingBoxData {
        return arguments
            ?.getParcelable(MapFragment.BOUNDING_BOX_DATA_KEY) ?: BoundingBoxData()
    }
}