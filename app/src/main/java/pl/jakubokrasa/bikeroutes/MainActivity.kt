package pl.jakubokrasa.bikeroutes

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val auth: FirebaseAuth by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.nav_record_route, R.id.nav_my_routes, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.setupWithNavController(navController)

        Log.d("MainActivity", "logged as " + auth.currentUser?.email.toString())

    }


}