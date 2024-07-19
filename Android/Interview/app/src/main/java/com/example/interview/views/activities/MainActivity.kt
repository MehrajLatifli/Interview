package com.example.interview.views.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.work.*
import com.example.interview.R
import com.example.interview.databinding.ActivityMainBinding
import com.example.interview.source.api.RefreshTokenDetector
import com.example.interview.utilities.gone
import com.example.interview.utilities.visible
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var refreshTokenDetector: RefreshTokenDetector

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
            ?: throw IllegalStateException("NavHostFragment not found in activity_main.xml")

        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.includeBottomnav.BottomNavigationView, navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.profileFragment, R.id.setting, R.id.operationFragment -> {
                    binding.includeBottomnav.BottomNavigationView.visible()
                }
                else -> {
                    binding.includeBottomnav.BottomNavigationView.gone()
                }
            }
        }

        refreshTokenDetector.startTokenRefreshing()


    }

    override fun onDestroy() {
        super.onDestroy()

        refreshTokenDetector.stop()
    }
}
