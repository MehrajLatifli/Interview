package com.example.interview.views.fragments.session

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentSessionCreateBinding
import com.example.interview.models.responses.post.session.SessionRequest
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.session.SessionViewModel
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

@AndroidEntryPoint
class SessionCreateFragment : BaseFragment<FragmentSessionCreateBinding>(FragmentSessionCreateBinding::inflate) {

    private val vacancyMap = mutableMapOf<String, String>()
    private val candidateMap = mutableMapOf<String, String>()
    private var selectedVacancyId: String = ""
    private var selectedCandidateId: String = ""
    private var sessionOwnerId: String = ""
    private var size: Int = 0

    private val viewModel by viewModels<SessionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        observeData()

        lifecycleScope.launch {
            initializeData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.vacancies.removeObservers(viewLifecycleOwner)
        viewModel.sessions.removeObservers(viewLifecycleOwner)
        viewModel.profiles.removeObservers(viewLifecycleOwner)
    }

    private fun setupUI() {
        val dropdownBackground: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.autocompletetextview_radiuscolor)
        binding.autocompleteVacancytextview.setDropDownBackgroundDrawable(dropdownBackground)
        binding.autocompleteCandidatetextview.setDropDownBackgroundDrawable(dropdownBackground)

        binding.autocompleteVacancytextview.setOnItemClickListener { _, _, position, _ ->
            val selectedItemName =
                binding.autocompleteVacancytextview.adapter.getItem(position) as? String
            selectedVacancyId = vacancyMap[selectedItemName] ?: ""
        }

        binding.autocompleteCandidatetextview.setOnItemClickListener { _, _, position, _ ->
            val selectedItemName =
                binding.autocompleteCandidatetextview.adapter.getItem(position) as? String
            selectedCandidateId = candidateMap[selectedItemName] ?: ""
        }

        binding.buttonCreate.setOnClickListener {
            lifecycleScope.launch {
                handleCreateSession()
            }
        }

        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)
        applySize(getPrimaryFontsize(), getSecondaryFontsize())
    }

    private suspend fun initializeData() {
        binding.includeProgressbar.progressBar.visible()
        binding.mainConstraintLayout.gone()
        binding.NestedScrollView.gone()

        viewModel.getprofile()

        size = viewModel.getAllOwnSession().size

        fetchAndSetupData()

        delay(2000)

        if (isAdded) {
            binding.includeProgressbar.progressBar.gone()
            binding.mainConstraintLayout.visible()
            binding.NestedScrollView.visible()
        }
    }

    private fun applySize(primaryFontSize: Float, secondaryFontSize: Float) {
        lifecycleScope.launch {
            if (isAdded) {
                binding.autocompleteVacancytextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, primaryFontSize)
                binding.autocompleteCandidatetextview.setTextSize(TypedValue.COMPLEX_UNIT_SP, primaryFontSize)
                binding.textInputLayout1.editText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, primaryFontSize)
                binding.textInputLayout2.editText?.setTextSize(TypedValue.COMPLEX_UNIT_SP, primaryFontSize)
                binding.buttonCreate.textSize = primaryFontSize
            }
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

    private fun applyTheme(themeName: String) {
        lifecycleScope.launch {
            if (isAdded) {

                if (themeName == "Secondary") {

                    binding.Main.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.color.Black
                    )
                    binding.NestedScrollView.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.color.Black
                    )

                    val colorStateList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.DeepPurple
                        )
                    )

                    binding.textInputLayout1.setHintTextColor(colorStateList)
                    binding.textInputLayout2.setHintTextColor(colorStateList)

                    val autoCompleteTextView = binding.autocompleteCandidatetextview
                    autoCompleteTextView.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.DeepPurple
                        )
                    )

                    val autoCompleteTextView2 = binding.autocompleteVacancytextview
                    autoCompleteTextView2.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.DeepPurple
                        )
                    )
                }

            }
        }
    }

    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }

    private fun observeData() {
        viewModel.profiles.observe(viewLifecycleOwner) { profiles ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                sessionOwnerId = profiles.firstOrNull()?.id.toString() ?: ""
                Log.e("SessionOwner", sessionOwnerId.toString())
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            lifecycleScope.launch {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {

                    if (isLoading) {
                        binding.includeProgressbar.progressBar.visible()
                        binding.mainConstraintLayout.gone()
                        binding.NestedScrollView.gone()
                    } else {
                        binding.includeProgressbar.progressBar.gone()
                        binding.mainConstraintLayout.visible()
                        binding.NestedScrollView.visible()
                    }
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank() && isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                if (size > 0) {
                    Log.e("VacancyCreate", errorMessage)
                    customResultDialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
                }
            }
        }

        viewModel.candidatedocuments.observe(viewLifecycleOwner) { candidatedocuments ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                candidateMap.clear()
                candidatedocuments.forEach { candidatedocument ->
                    candidateMap[candidatedocument.name] = candidatedocument.id.toString()
                }
                setupAutoCompleteTextView()
            }
        }

        viewModel.vacancies.observe(viewLifecycleOwner) { vacancies ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                vacancyMap.clear()
                vacancies.forEach { vacancy ->
                    vacancyMap[vacancy.title] = vacancy.id.toString()
                }
                setupAutoCompleteTextView()
            }
        }

        viewModel.completeResult.observe(viewLifecycleOwner) { completeResult ->
            lifecycleScope.launch {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    if (completeResult) {
                        customResultDialog(
                            "Successful!",
                            "Please wait a moment, we are preparing for you...",
                            R.color.DeepPurple
                        )
                        delay(2500)
                        findNavController().navigate(SessionCreateFragmentDirections.actionSessionCreateFragmentToSessionReadFragment())
                    } else {
                        delay(2500)
                        findNavController().navigate(SessionCreateFragmentDirections.actionSessionCreateFragmentToOperationFragment())
                    }
                }
            }
        }
    }

    private suspend fun fetchAndSetupData() {
        if (isAdded) {
            val vacancies = viewModel.getAllVacancies()
            vacancyMap.clear()
            vacancies.forEach { vacancy ->
                vacancyMap[vacancy.title] = vacancy.id.toString()
            }
            setupAutoCompleteTextView()

            val candidateDocuments = viewModel.getAllCandidateDocuments()
            candidateMap.clear()
            candidateDocuments.forEach { candidate ->
                candidateMap[candidate.name] = candidate.id.toString()
            }
            setupAutoCompleteTextView()
        }
    }

    private fun setupAutoCompleteTextView() {
        if (isAdded) {
            val vacancyAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdownlist, vacancyMap.keys.toList())
            binding.autocompleteVacancytextview.setAdapter(vacancyAdapter)

            val candidateAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdownlist, candidateMap.keys.toList())
            binding.autocompleteCandidatetextview.setAdapter(candidateAdapter)
        }
    }

    private fun customResultDialog(title: String, text: String, colorId: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            val dialogBinding = CustomresultdialogBinding.inflate(LayoutInflater.from(requireContext()))
            val dialog = AlertDialog.Builder(requireContext()).apply {
                setView(dialogBinding.root)
            }.create()

            dialogBinding.itemimageView.loadImageWithGlideAndResize(R.drawable.registerstatuse, requireContext())
            dialogBinding.titleTextView.text = title
            dialogBinding.titleTextView.setTextColor(ContextCompat.getColor(requireContext(), colorId))
            dialogBinding.textTextView.text = text
            dialogBinding.textTextView.setTextColor(ContextCompat.getColor(requireContext(), colorId))

            dialog.show()
            delay(2000)
            dialog.dismiss()
        }
    }

    private suspend fun handleCreateSession(): Boolean {
        if (selectedVacancyId.isBlank() || selectedCandidateId.isBlank() || sessionOwnerId.isBlank()) {
            customResultDialog("Unsuccessful!", "Please fill all required fields", R.color.MellowMelon)
            return false
        }

        val vacancyId = selectedVacancyId.toIntOrNull() ?: return false
        val candidateId = selectedCandidateId.toIntOrNull() ?: return false
        val userId = sessionOwnerId.toIntOrNull() ?: return false

        return try {
            val sessionRequest = SessionRequest(
                endValue = 0,
                startDate = LocalDateTime.now().toString(),
                endDate = null,
                vacancyId = vacancyId,
                candidateId = candidateId,
                userId = userId
            )

            viewModel.addSession(sessionRequest)
            Log.d("SessionCreateFragment", "Session creation requested")
            true
        } catch (e: Exception) {
            Log.e("SessionCreateFragment", "Error: ${e.message}")
            customResultDialog("Unsuccessful!", "An error occurred", R.color.MellowMelon)
            false
        }
    }
}
