package com.example.interview.views.fragments.operation

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.FragmentOperationBinding
import com.example.interview.models.localadapdermodels.operationcrud.Operation
import com.example.interview.models.localadapdermodels.operationtype.OperationType
import com.example.interview.utilities.gone
import com.example.interview.views.adapters.operationtype.OperationTypeAdapder
import com.example.interview.views.fragments.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OperationFragment : BaseFragment<FragmentOperationBinding>(
    FragmentOperationBinding::inflate) {

    private val operations = arrayListOf(
        Operation("Create", R.drawable.create),
        Operation("Read", R.drawable.read)
    )

    private val operationTypeList = arrayListOf(
        OperationType(R.drawable.candidate, "Candidate", operations),
        OperationType(R.drawable.vacancy, "Vacancy", operations)
    )

    private val operationTypeAdapder = OperationTypeAdapder()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.includeProgressbar.progressBar.gone()

        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        operationTypeAdapder.itemClickHandler = { selectedItemText, operationTypeText ->
            lifecycleScope.launch(Dispatchers.Main) {
                if (isAdded && !isStateSaved) {
                    delay(300)
                    navigateToFragment(selectedItemText, operationTypeText)
                }
            }
        }

        binding.rvOperationtype.adapter = operationTypeAdapder
        operationTypeAdapder.updateList(operationTypeList)

        applySize(getPrimaryFontsize(), getSecondaryFontsize())
    }

    private fun navigateToFragment(selectedItemText: String, operationTypeText: String) {
        val action = when {
            operationTypeText == "Candidate" && selectedItemText == "Create" ->
                OperationFragmentDirections.actionOperationFragmentToCandidateCreateFragment()
            operationTypeText == "Candidate" && selectedItemText == "Read" ->
                OperationFragmentDirections.actionOperationFragmentToCandidateReadFragment()
            operationTypeText == "Vacancy" && selectedItemText == "Create" ->
                OperationFragmentDirections.actionOperationFragmentToVacancyCreateFragment()
            operationTypeText == "Vacancy" && selectedItemText == "Read" ->
                OperationFragmentDirections.actionOperationFragmentToVacancyReadFragment()
            else -> null
        }
        action?.let { findNavController().navigate(it) }
    }

    private fun applyTheme(themeName: String) {
        lifecycleScope.launch {
            if (themeName == "Secondary") {
                binding.Main.background = ContextCompat.getDrawable(requireContext(), R.color.bottom_nav_color2_2)
                binding.NestedScrollView.background = ContextCompat.getDrawable(requireContext(), R.color.bottom_nav_color2_2)
            }
        }
    }

    private fun applySize(primaryFontSize: Float, secondaryFontSize: Float) {
        binding?.let {
            operationTypeAdapder.setFontSizes(primaryFontSize, secondaryFontSize)
        }
    }

    private fun getPrimaryFontsize(): Float {
        val sp = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sp.getFloat("primaryFontsize", 16.0F)
    }

    private fun getSecondaryFontsize(): Float {
        val sp = requireActivity().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sp.getFloat("secondaryFontsize", 12.0F)
    }

    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }
}

