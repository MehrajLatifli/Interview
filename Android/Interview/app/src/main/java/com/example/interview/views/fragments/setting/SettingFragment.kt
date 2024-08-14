package com.example.interview.views.fragments.setting

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.interview.R
import com.example.interview.databinding.FragmentSessionReadBinding
import com.example.interview.databinding.FragmentSettingBinding
import com.example.interview.viewmodels.session.SessionViewModel
import com.example.interview.viewmodels.setting.SettingViewModel
import com.example.interview.views.activities.MainActivity
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>(FragmentSettingBinding::inflate) {

    private val viewModel by viewModels<SettingViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val defaultFontSize1 = 16.0F
        val defaultFontSize2 = 12.0F

        val savedPrimaryFontSize = getPrimaryFontsize()?:defaultFontSize1
        val savedSecondaryFontSize = getSecondaryFontsize()?:defaultFontSize2
        val savedThemeName = getThemeName()?:"Primary"
        binding.themeNametextView.text=savedThemeName


        val isThemeSecondary = savedThemeName == "Secondary"
        binding.themeswitch.isChecked = isThemeSecondary


        updateThemeUI(isThemeSecondary)

        binding.fontsizecounttextView1.text = String.format("%.1f", savedPrimaryFontSize)
        binding.sliderPrimaryFontSize.value = savedPrimaryFontSize
        viewModel.setPrimaryFontSize(savedPrimaryFontSize / 8.0f)


        binding.sliderPrimaryFontSize.addOnChangeListener { _, value, _ ->
            val sizeMultiplier = (value / 8.0f).coerceIn(0.8f, 8.0f)
            viewModel.setPrimaryFontSize(sizeMultiplier)
            binding.fontsizecounttextView1.text = String.format("%.1f", value)


            binding.fontsizecounttextView1.textSize = value

            savePrimaryFontsize(value) // Save the updated value


        }

        viewModel.primaryfontSize.observe(viewLifecycleOwner) { sizeMultiplier ->
            val newFontSize = (sizeMultiplier * 8).coerceIn(8.0F, 20.0F)
            binding.fontsizecounttextView1.text = String.format("%.1f", newFontSize)
            binding.sliderPrimaryFontSize.value = newFontSize
        }

        binding.fontsizecounttextView2.text = String.format("%.1f", savedSecondaryFontSize)
        binding.sliderSecondaryFontSize.value = savedSecondaryFontSize
        viewModel.setSecondaryFontSize(savedSecondaryFontSize / 8.0f)

        binding.sliderSecondaryFontSize.addOnChangeListener { _, value, _ ->
            val sizeMultiplier = (value / 8.0f).coerceIn(0.8f, 8.0f)
            viewModel.setSecondaryFontSize(sizeMultiplier)
            binding.fontsizecounttextView2.text = String.format("%.1f", value)


            binding.fontsizecounttextView2.textSize = value

            saveSecondaryFontsize(value)
        }

        viewModel.secondaryfontSize.observe(viewLifecycleOwner) { sizeMultiplier ->
            val newFontSize = (sizeMultiplier * 8).coerceIn(8.0F, 20.0F)
            binding.fontsizecounttextView2.text = String.format("%.1f", newFontSize)
            binding.sliderSecondaryFontSize.value = newFontSize
        }

        binding.themeswitch.setOnCheckedChangeListener { _, isChecked ->
            val intent = Intent(requireContext(), MainActivity::class.java)

            if (isChecked) {
                binding.themeNametextView.text = "Secondary"
                saveThemeName("Secondary")
                startActivity(intent)
                requireActivity().finish()

            } else {
                binding.themeNametextView.text = "Primary"
                saveThemeName("Primary")
                startActivity(intent)
                requireActivity().finish()
            }
            updateThemeUI(isChecked)
        }

        binding.buttonReset.setOnClickListener {
            // Define default values
            val defaultPrimaryFontSize = 20.0F
            val defaultSecondaryFontSize = 16.0F
            val defaultThemeName = "Primary"

            // Reset SharedPreferences
            val sharedPreferences = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                remove("primaryFontsize")
                remove("secondaryFontsize")
                remove("themeName")
                apply()
            }

            // Reset ViewModel
            viewModel?.setPrimaryFontSize(defaultPrimaryFontSize / 8.0f)
            viewModel?.setSecondaryFontSize(defaultSecondaryFontSize / 8.0f)

            // Update UI
            binding?.let {


                it.fontsizecounttextView1.text = String.format("%.1f", defaultPrimaryFontSize)
                it.sliderPrimaryFontSize.value = defaultPrimaryFontSize
                it.primaryfontsizetextView.textSize = savedPrimaryFontSize

                it.fontsizecounttextView2.text =String.format("%.1f", defaultSecondaryFontSize)
                it.sliderSecondaryFontSize.value = defaultSecondaryFontSize
                it.secondaryfontsizetextView.textSize = savedSecondaryFontSize


                savePrimaryFontsize(defaultPrimaryFontSize)
                saveSecondaryFontsize(defaultSecondaryFontSize)



                it.themeNametextView.text = defaultThemeName
                it.themeswitch.isChecked = false

                updateThemeUI(false)
            }
        }


        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        applySize(getPrimaryFontsize(),getSecondaryFontsize())

    }

    private fun applySize(savedPrimaryFontSize: Float, savedSecondaryFontSize:Float) {

        binding?.let {
            it.primaryfontsizetextView.textSize = savedPrimaryFontSize
            it.fontsizecounttextView1.textSize = savedPrimaryFontSize

            it.secondaryfontsizetextView.textSize = savedPrimaryFontSize
            it.fontsizecounttextView2.textSize = savedPrimaryFontSize

            it.themetextView.textSize = savedPrimaryFontSize
            it.themeNametextView.textSize = savedPrimaryFontSize

        }
    }

    private fun applyTheme(themeName: String) {
        lifecycleScope.launch {
            if (themeName == "Secondary") {
                binding.Main.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.bottom_nav_color2_2
                )
                binding.NestedScrollView.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.bottom_nav_color2_2
                )
            }
        }
    }





    private fun updateThemeUI(isSecondaryTheme: Boolean) {
        if (isSecondaryTheme) {
            binding.themeswitch.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Black))
            binding.themeswitch.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.DeepPurple))

        } else {
            binding.themeswitch.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.DeepPurple))
            binding.themeswitch.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.Black))
        }
    }


    private fun savePrimaryFontsize(primaryFontsize: Float) {
        val sharedPreferences = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putFloat("primaryFontsize", primaryFontsize)
            apply()
        }
    }

    private fun getPrimaryFontsize(): Float {
        val sp = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sp.getFloat("primaryFontsize", 16.0F)
    }




    private fun saveSecondaryFontsize(secondaryFontsize: Float) {
        val sharedPreferences = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putFloat("secondaryFontsize", secondaryFontsize)
            apply()
        }
    }

    private fun getSecondaryFontsize(): Float {
        val sp = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)

        return sp.getFloat("secondaryFontsize", 12.0F)
    }

    private fun saveThemeName(themeName: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("themeName", themeName)
            apply()
        }
    }

    private fun getThemeName(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }




}




