package com.example.interview.views.fragments.session

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentHomeBinding
import com.example.interview.databinding.FragmentSessionCreateBinding
import com.example.interview.models.responses.post.session.SessionRequest
import com.example.interview.models.responses.post.sessionquestion.RandomQuestionRequest2
import com.example.interview.models.responses.post.vacancy.VacancyRequest
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.session.SessionViewModel
import com.example.interview.viewmodels.vacancy.VacancyViewModel
import com.example.interview.views.fragments.base.BaseFragment
import com.example.interview.views.fragments.vacancy.VacancyCreateFragmentDirections
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

        val dropdownBackground: Drawable? =
            ContextCompat.getDrawable(requireContext(), R.drawable.autocompletetextview_radiuscolor)
        binding.autocompleteVacancytextview.setDropDownBackgroundDrawable(dropdownBackground)
        binding.autocompleteCandidatetextview.setDropDownBackgroundDrawable(dropdownBackground)


        lifecycleScope.launch() {


            binding.includeProgressbar.progressBar.visible()
            binding.mainConstraintLayout.gone()
            binding.NestedScrollView.gone()


            viewModel.getprofile()

            observeData()

           size=viewModel.getAllOwnSession().size

            fetchAndSetupData()

            delay(2000)


            binding.includeProgressbar.progressBar.gone()
            binding.mainConstraintLayout.visible()
            binding.NestedScrollView.visible()


        }




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


    }

    private fun observeData() {

        viewModel.profiles.observe(viewLifecycleOwner) { profiles ->
            if (profiles.isNotEmpty()) {
                sessionOwnerId = profiles.firstOrNull()?.id.toString() ?: ""
                Log.e("SessionOwner", sessionOwnerId.toString())
            } else {
                Log.e("SessionOwner", "No profiles found")
            }
        }

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            lifecycleScope.launch(Dispatchers.Main) {
                delay(2000)
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

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank()) {


                if (size>0) {
                    Log.e("VacancyCreate", errorMessage)
                    customResultDialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
                }
            }
        }

        viewModel.candidatedocuments.observe(viewLifecycleOwner) { candidatedocuments ->
            candidateMap.clear()
            candidatedocuments.forEach { candidatedocument ->
                candidateMap[candidatedocument.name] = candidatedocument.id.toString()
            }
            setupAutoCompleteTextView()
        }

        viewModel.vacancies.observe(viewLifecycleOwner) { vacancies ->
            vacancyMap.clear()
            vacancies.forEach { vacancy ->
                vacancyMap[vacancy.title] = vacancy.id.toString()
            }
            setupAutoCompleteTextView()
        }




        viewModel.completeResult.observe(viewLifecycleOwner) { completeResult ->
            lifecycleScope.launch {
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

    private suspend fun fetchAndSetupData() {
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

    private fun setupAutoCompleteTextView() {
        val vacancyAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdownlist, vacancyMap.keys.toList())
        binding.autocompleteVacancytextview.setAdapter(vacancyAdapter)

        val candidateAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdownlist, candidateMap.keys.toList())
        binding.autocompleteCandidatetextview.setAdapter(candidateAdapter)
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
            customResultDialog(
                "Unsuccessful!",
                "Please fill all required fields",
                R.color.MellowMelon
            )
            return false
        }

        val vacancyId = selectedVacancyId.toIntOrNull() ?: return false
        val candidateId = selectedCandidateId.toIntOrNull() ?: return false
        val userId = sessionOwnerId.toIntOrNull() ?: return false

        try {
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

            return true


        } catch (e: Exception) {
            Log.e("SessionCreateFragment", "Error: ${e.message}")
            customResultDialog("Unsuccessful!", "An error occurred", R.color.MellowMelon)
            return false
        }
    }






}