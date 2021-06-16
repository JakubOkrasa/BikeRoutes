package pl.jakubokrasa.bikeroutes.features.map.presentation

import android.os.Bundle
import android.view.View
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_DISTANCE_SUM
import pl.jakubokrasa.bikeroutes.core.extensions.hideKeyboard
import pl.jakubokrasa.bikeroutes.core.util.enums.SharingType
import pl.jakubokrasa.bikeroutes.databinding.FragmentSaveRouteBinding
import pl.jakubokrasa.bikeroutes.features.map.domain.usecase.DataSaveRoute
import pl.jakubokrasa.bikeroutes.features.map.navigation.MapFrgNavigator

class SaveRouteFragment : BaseFragment<MapViewModel>(R.layout.fragment_save_route) {

    private var _binding: FragmentSaveRouteBinding? = null
    private val binding get() = _binding!!
    private val mapFrgNavigator: MapFrgNavigator by inject()
    override val viewModel: MapViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSaveRouteBinding.bind(view)
        binding.btSave.setOnClickListener(btSaveOnClick)
    }

    private val btSaveOnClick = View.OnClickListener() {
        val name = binding.etName.text.toString()
        val description = binding.etDescription.text.toString()
        val distance = preferenceHelper.preferences.getInt(PREF_KEY_DISTANCE_SUM, 0)
        val sharingType = if (binding.swPrivate.isChecked) SharingType.PRIVATE else SharingType.PUBLIC
        if(name.isEmpty()) showToast("Route must have a name")
        else  {
            viewModel.saveRoute(DataSaveRoute(name, description, distance, sharingType))
        }
        hideKeyboard()
        mapFrgNavigator.goBack()
    }
}