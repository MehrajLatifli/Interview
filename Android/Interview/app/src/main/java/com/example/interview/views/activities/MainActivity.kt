package com.example.interview.views.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.interview.R
import com.example.interview.databinding.ActivityMainBinding
import com.example.interview.source.api.RefreshTokenDetector
import com.example.interview.utilities.gone
import com.example.interview.utilities.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var refreshTokenDetector: RefreshTokenDetector

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
            ?: throw IllegalStateException("NavHostFragment not found in activity_main.xml")

        navController = navHostFragment.navController

        // Setup bottom navigation view with NavController
        NavigationUI.setupWithNavController(binding.includeBottomnav.BottomNavigationView, navController)

        // Add destination change listener to manage bottom navigation visibility
        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("MainActivity", "Current destination: ${destination.id}")
            if (!supportFragmentManager.isStateSaved) {
                when (destination.id) {
                    R.id.homeFragment,
                    R.id.profileFragment,
                    R.id.operationFragment,
                    R.id.candidateCreateFragment -> {
                        binding.includeBottomnav.BottomNavigationView.visible()
                    }
                    else -> {
                        binding.includeBottomnav.BottomNavigationView.gone()
                    }
                }
            }
        }

        refreshTokenDetector.startTokenRefreshing()
    }

    override fun onResume() {
        super.onResume()
        // Add any additional logic needed when the activity resumes
    }

    override fun onDestroy() {
        super.onDestroy()
        refreshTokenDetector.stop()
    }
}
