package com.example.interview.views.fragments.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.interview.R
import com.example.interview.databinding.FragmentHomeBinding
import com.example.interview.utilities.Constants.API_KEY
import com.example.interview.utilities.visible
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment  : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


       // API_KEY=loadApiKey(requireContext()).toString()


        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)
    }

    private fun applyTheme(themeName: String) {
        lifecycleScope.launch {
            if (themeName == "Secondary") {
                binding.Main.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.bottom_nav_color2_2
                )
//                binding.NestedScrollView.background = ContextCompat.getDrawable(
//                    requireContext(),
//                    R.color.bottom_nav_color2_2
//                )
            }
        }
    }

    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }
}