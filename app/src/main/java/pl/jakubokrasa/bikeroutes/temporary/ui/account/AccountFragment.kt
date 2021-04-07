package pl.jakubokrasa.bikeroutes.temporary.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.extensions.viewBinding
import pl.jakubokrasa.bikeroutes.databinding.FragmentAccountBinding
import pl.jakubokrasa.bikeroutes.features.user.ui.SignUpActivity
import pl.jakubokrasa.bikeroutes.temporary.ui.dashboard.DashboardViewModel

class AccountFragment : Fragment() {
//    private val binding by viewBinding(FragmentAccountBinding::bind) // from AA, stopped working

    //from https://developer.android.com/topic/libraries/view-binding
private var _binding: FragmentAccountBinding? = null
    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root


//        val bt: Button = root.findViewById(R.id.bt_account)
//
//        bt.setOnClickListener {
//            Log.d("LOG", "btAccount clicked")
//            val intent = Intent(requireContext(), SignUpActivity::class.java)
//            startActivity(intent)
//        }

        binding.btAccount.setOnClickListener {
            Log.d("LOG", "btAccount clicked")
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }


        return view
    }
}