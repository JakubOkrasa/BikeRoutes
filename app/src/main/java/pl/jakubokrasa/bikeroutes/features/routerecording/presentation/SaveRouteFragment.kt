package pl.jakubokrasa.bikeroutes.features.routerecording.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extentions.PreferenceManager
import pl.jakubokrasa.bikeroutes.core.extentions.hideKeyboard
import pl.jakubokrasa.bikeroutes.databinding.FragmentSaveRouteBinding
import pl.jakubokrasa.bikeroutes.features.routerecording.domain.DataRouteSave
import pl.jakubokrasa.bikeroutes.features.routerecording.presentation.RecordRouteFragment.Companion.PREF_KEY_DISTANCE_SUM

class SaveRouteFragment : Fragment(R.layout.fragment_save_route) {

    private var _binding: FragmentSaveRouteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RouteViewModel by sharedViewModel()
    private val preferenceManager: PreferenceManager by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSaveRouteBinding.bind(view)
        binding.btSave.setOnClickListener(btSaveOnClick)

    }

    private val btSaveOnClick = View.OnClickListener() {
        val name = binding.etName.text.toString()
        val distance = preferenceManager.preferences.getInt(PREF_KEY_DISTANCE_SUM, 0)
        if(name.isEmpty()) {
            Toast.makeText(context, "Name the route!", Toast.LENGTH_LONG).show()
        } else {
            viewModel.putRouteSaveData(DataRouteSave(0, name, binding.etDescription.text.toString(), distance))
            viewModel.markRouteAsNotCurrent()
        }
        hideKeyboard()
        parentFragmentManager.popBackStack()
    }
}