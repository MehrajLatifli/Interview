package com.example.interview.views.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.interview.R
import com.example.interview.databinding.ActivityMainBinding
import com.example.interview.utilities.gone
import com.example.interview.utilities.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

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

        // Observe navigation changes
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.profileFragment, R.id.setting, R.id.operations -> {
                    binding.includeBottomnav.BottomNavigationView.visible()
                }
                else -> {
                    binding.includeBottomnav.BottomNavigationView.gone()
                }
            }
        }
    }
}
