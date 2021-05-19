package pl.jakubokrasa.bikeroutes.core.user.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import pl.jakubokrasa.bikeroutes.R
import pl.jakubokrasa.bikeroutes.core.base.platform.BaseFragment
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_EMAIL
import pl.jakubokrasa.bikeroutes.core.extensions.PreferenceHelper.Companion.PREF_KEY_USER_PASSWORD
import pl.jakubokrasa.bikeroutes.databinding.FragmentAccountBinding

class AccountFragment(): BaseFragment<UserViewModel>(R.layout.fragment_account) {
    private val auth: FirebaseAuth by inject()
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!
    override val viewModel: UserViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        binding.tvLoggedAs.text = String.format("you are logged as %s", auth.currentUser?.email)

        binding.btLogout.setOnClickListener {
            viewModel.logOut()
        }

        binding.btSuggestion.setOnClickListener {
            openWebsite("https://forms.gle/aaFoxM1aMCfGVAEM7")
        }

        binding.btReport.setOnClickListener {
            openWebsite("https://forms.gle/YuA8XH6Rewk4bEir6")
        }
    }

    private fun openWebsite(url: String) {
        var url = url
        try{
            if (!url.startsWith("http://") && !url.startsWith("https://")) url = "http://$url";
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        } catch(e: Exception) {
            Toast.makeText(context, "An error occrured", Toast.LENGTH_SHORT).show()
        }

    }
}