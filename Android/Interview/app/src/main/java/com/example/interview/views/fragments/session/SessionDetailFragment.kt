package com.example.interview.views.fragments.session

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentSessionCreateBinding
import com.example.interview.databinding.FragmentSessionDetailBinding
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.session.SessionViewModel
import com.example.interview.viewmodels.vacancy.VacancyViewModel
import com.example.interview.views.fragments.base.BaseFragment
import com.example.interview.views.fragments.vacancy.VacancyDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SessionDetailFragment : BaseFragment<FragmentSessionDetailBinding>(FragmentSessionDetailBinding::inflate) {

    private val args: SessionDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<SessionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.includeProgressbar.progressBar.visible()
        binding.NestedScrollView.gone()

        lifecycleScope.launch {
            delay(2000)

            args.session.id?.let { sessionId -> viewModel.getSessionByID(sessionId) }
            args.session.vacancyId?.let { vacancyId -> viewModel.getVacancyByID(vacancyId) }
            args.session.candidateId?.let { candidateId -> viewModel.getCandidateDocumentByID(candidateId) }
            args.session.userId?.let { viewModel.getprofile() }


            observeData()

            binding.includeProgressbar.progressBar.gone()
            binding.NestedScrollView.visible()
        }






    }

    private fun observeData() {

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.includeProgressbar.progressBar.visible()
                binding.NestedScrollView.gone()
            } else {
                binding.includeProgressbar.progressBar.gone()
                binding.NestedScrollView.visible()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank()) {
                Log.e("VacancyViewModel", errorMessage)
                customresultdialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
            }
        }

        viewModel.session.observe(viewLifecycleOwner) { item ->
            item?.let {

                binding.textView1.text = it.endValue.toString() ?: ""

                if(!it.startDate.isNullOrEmpty())
                {
                    binding.textView2.text = formatDateTime(it.startDate ?: "")
                }

                if(!it.endDate.isNullOrEmpty()) {

                    binding.textView3.text = formatDateTime(it.endDate ?: "")
                }
            }
        }

        viewModel.vacancy.observe(viewLifecycleOwner) { vacancy ->
            binding.textView4.text = vacancy?.title ?: ""
        }

        viewModel.candidateDocument.observe(viewLifecycleOwner) { candidateDocument ->
            binding.textView5.text = candidateDocument?.name ?: ""
        }

        viewModel.profiles.observe(viewLifecycleOwner) { profiles ->
            val profilesText = profiles.map { profile ->
                "${profile.username ?: ""}"
            }.joinToString(separator = "\n\n")

            binding.textView6.text = profilesText
        }
    }

    fun formatDateTime(dateTimeString: String): String {
        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy   HH:mm:ss.SSS")


        val dateTime = LocalDateTime.parse(dateTimeString, inputFormatter)

        return dateTime.format(outputFormatter)
    }

    private fun customresultdialog(title: String, text: String, colorId: Int) {
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
}