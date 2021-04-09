package pl.jakubokrasa.bikeroutes.features.map.presentation

import android.os.Bundle
import android.view.View
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.hideKeyboard
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_DISTANCE_SUM
import pl.jakubokrasa.bikeroutes.databinding.FragmentSaveRouteBinding
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.DataRouteSave

class SaveRouteFragment : BaseFragment(R.layout.fragment_save_route) {

    private var _binding: FragmentSaveRouteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSaveRouteBinding.bind(view)
        binding.btSave.setOnClickListener(btSaveOnClick)
    }

    private val btSaveOnClick = View.OnClickListener() {
        val name = binding.etName.text.toString()
        val description = binding.etDescription.text.toString()
        val distance = preferenceHelper.preferences.getInt(PREF_KEY_DISTANCE_SUM, 0)
        if(name.isEmpty()) showToast("Route must have a name")
        else viewModel.putRouteSaveData(DataRouteSave(name, description, distance))
        hideKeyboard()
        parentFragmentManager.popBackStack()
    }
}