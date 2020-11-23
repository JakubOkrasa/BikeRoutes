package pl.jakubokrasa.bikeroutes.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.jakubokrasa.bikeroutes.R

class RecordRouteFragment : Fragment() {

    private lateinit var recordRouteViewModel: RecordRouteViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        recordRouteViewModel = ViewModelProvider(this).get(RecordRouteViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_record_route, container, false)
        val textView: TextView = root.findViewById(R.id.tv_record_route)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}