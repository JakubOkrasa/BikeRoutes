package pl.jakubokrasa.bikeroutes.features.routerecording.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.databinding.FragmentSaveRouteBinding

class SaveRouteFragment : Fragment(R.layout.fragment_save_route) {

    private var _binding: FragmentSaveRouteBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSaveRouteBinding.bind(view)

        binding.btSave.setOnClickListener(btSaveOnClick)

    }

    private val btSaveOnClick = View.OnClickListener() {
            // update route in db - put name, distance
            // mark route as not current
    }
}