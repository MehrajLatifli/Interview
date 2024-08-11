package com.example.interview.views.activities

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.interview.R
import com.example.interview.databinding.ActivityMainBinding
import com.example.interview.source.api.RefreshTokenDetector
import com.example.interview.utilities.gone
import com.example.interview.utilities.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var refreshTokenDetector: RefreshTokenDetector

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var isTransitionInProgress = false // Flag to track transition state
    private val handler = Handler(Looper.getMainLooper())
    private var isDebouncing = false
    private val debounceDelay: Long = 100 // 1 second

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as? NavHostFragment
                ?: throw IllegalStateException("NavHostFragment not found in activity_main.xml")

        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(
            binding.includeBottomnav.BottomNavigationView,
            navController
        )

        binding.includeBottomnav.BottomNavigationView.gone()

        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("MainActivity", "Current destination: ${destination.id}")

            // Avoid overlapping transitions
            if (isTransitionInProgress) return@addOnDestinationChangedListener

            isTransitionInProgress = true
            lifecycleScope.launch {
                delay(1000) // 1-second delay
                if (!supportFragmentManager.isStateSaved) {
                    if (!isFinishing && !isDestroyed) {
                        when (destination.id) {
                            R.id.homeFragment,
                            R.id.profileFragment,
                            R.id.operationFragment,
                            R.id.candidateReadFragment,
                            R.id.candidateCreateFragment,
                            R.id.candidateDetailFragment,
                            R.id.candidateUpdateFragment,
                            R.id.vacancyCreateFragment,
                            R.id.vacancyReadFragment,
                            R.id.vacancyUpdateFragment,
                            R.id.vacancyDetailFragment,
                            R.id.sessionCreateFragment,
                            R.id.sessionReadFragment,
                            R.id.sessionUpdateFragment,
                            R.id.sessionDetailFragment,
                            R.id.settingFragment -> {
                                showBottomNavigationView()
                            }

                            else -> {
                                hideBottomNavigationView()
                            }
                        }
                    }
                    isTransitionInProgress = false
                }
            }
        }

        refreshTokenDetector.startTokenRefreshing()
    }

    override fun onResume() {
        super.onResume()
        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)
    }

    override fun onDestroy() {
        super.onDestroy()
        refreshTokenDetector.stop()
    }

    private fun getThemeName(): String? {
        val sharedPreferences = getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }

    private fun applyTheme(themeName: String) {
        lifecycleScope.launch {
            val iconTintList: ColorStateList
            val activeIndicatorColor: ColorStateList
            val rippleColorList: ColorStateList

            delay(500)
            if (themeName == "Secondary") {
                iconTintList =
                    ContextCompat.getColorStateList(this@MainActivity, R.color.bottom_nav_color2)
                        ?: ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.ColdGrey
                            )
                        )

                binding.includeBottomnav.BottomNavigationView.background =
                    ContextCompat.getDrawable(this@MainActivity, R.color.bottom_nav_color2_2)

                activeIndicatorColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.MellowMelon
                    )
                )
                rippleColorList =
                    ContextCompat.getColorStateList(this@MainActivity, R.color.ColdGrey)
                        ?: ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.ColdGrey
                            )
                        )
            } else {
                iconTintList =
                    ContextCompat.getColorStateList(this@MainActivity, R.color.bottom_nav_color)
                        ?: ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.ColdGrey
                            )
                        )
                activeIndicatorColor = ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this@MainActivity,
                        R.color.MellowMelon
                    )
                )

                binding.includeBottomnav.BottomNavigationView.background =
                    ContextCompat.getDrawable(this@MainActivity, R.color.bottom_nav_color1_2)

                rippleColorList =
                    ContextCompat.getColorStateList(this@MainActivity, R.color.MellowMelon)
                        ?: ColorStateList.valueOf(
                            ContextCompat.getColor(
                                this@MainActivity,
                                R.color.ColdGrey
                            )
                        )
            }

            binding.includeBottomnav.BottomNavigationView.itemIconTintList = iconTintList
            binding.includeBottomnav.BottomNavigationView.itemRippleColor = rippleColorList
        }
    }

    private fun showBottomNavigationView() {
        binding.includeBottomnav.BottomNavigationView.visible()
        binding.includeBottomnav.BottomNavigationView.setOnItemSelectedListener { item ->
            if (isDebouncing) {
                false
            } else {
                isDebouncing = true
                handleNavigationItemSelection(item.itemId)
                handler.postDelayed({ isDebouncing = false }, debounceDelay)
                true
            }
        }
    }

    private fun handleNavigationItemSelection(itemId: Int): Boolean {
        when (itemId) {
            R.id.homeFragment -> navController.navigate(R.id.homeFragment)
            R.id.profileFragment -> navController.navigate(R.id.profileFragment)
            R.id.operationFragment -> navController.navigate(R.id.operationFragment)
            R.id.candidateReadFragment -> navController.navigate(R.id.candidateReadFragment)
            R.id.candidateCreateFragment -> navController.navigate(R.id.candidateCreateFragment)
            R.id.candidateDetailFragment -> navController.navigate(R.id.candidateDetailFragment)
            R.id.candidateUpdateFragment -> navController.navigate(R.id.candidateUpdateFragment)
            R.id.vacancyCreateFragment -> navController.navigate(R.id.vacancyCreateFragment)
            R.id.vacancyReadFragment -> navController.navigate(R.id.vacancyReadFragment)
            R.id.vacancyUpdateFragment -> navController.navigate(R.id.vacancyUpdateFragment)
            R.id.vacancyDetailFragment -> navController.navigate(R.id.vacancyDetailFragment)
            R.id.sessionCreateFragment -> navController.navigate(R.id.sessionCreateFragment)
            R.id.sessionReadFragment -> navController.navigate(R.id.sessionReadFragment)
            R.id.sessionUpdateFragment -> navController.navigate(R.id.sessionUpdateFragment)
            R.id.sessionDetailFragment -> navController.navigate(R.id.sessionDetailFragment)
            R.id.settingFragment -> navController.navigate(R.id.settingFragment)
            else -> false
        }
        return true
    }

    private fun hideBottomNavigationView() {
        binding.includeBottomnav.BottomNavigationView.gone()
    }
}
