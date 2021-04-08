package pl.jakubokrasa.bikeroutes.core.user.ui

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.BaseFragment
import pl.jakubokrasa.bikeroutes.databinding.FragmentAccountBinding

class AccountFragment : BaseFragment(R.layout.fragment_account) {
    private val auth: FirebaseAuth by inject()
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        binding.tvLoggedAs.text = "you are logged as" + auth.currentUser?.email.toString()
    }
}