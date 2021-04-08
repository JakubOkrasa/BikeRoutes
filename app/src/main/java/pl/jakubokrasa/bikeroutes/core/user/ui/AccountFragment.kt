package pl.jakubokrasa.bikeroutes.core.user.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.FragmentAccountBinding

class AccountFragment : BaseFragment(R.layout.fragment_account) {
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        binding.btAccount.setOnClickListener {
            Log.d("LOG", "btAccount clicked")
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}