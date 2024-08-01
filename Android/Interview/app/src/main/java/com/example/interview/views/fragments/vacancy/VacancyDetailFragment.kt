package com.example.interview.views.fragments.vacancy

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
import com.example.interview.databinding.FragmentVacancyCreateBinding
import com.example.interview.databinding.FragmentVacancyDetailBinding
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.candidate.CandidateViewModel
import com.example.interview.viewmodels.vacancy.VacancyViewModel
import com.example.interview.views.fragments.base.BaseFragment
import com.example.interview.views.fragments.candidate.CandidateDetailFragmentArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class VacancyDetailFragment : BaseFragment<FragmentVacancyDetailBinding>(FragmentVacancyDetailBinding::inflate) {

    private val args: VacancyDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<VacancyViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getVacancyByID(args.vacancy.id)

        viewModel.getPositionByID(args.vacancy.positionId)
        viewModel.getStructureByID(args.vacancy.structureId)

        observeData()

    }

    private fun observeData() {
        viewModel.vacancy.observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.textView1.text = it.title ?: ""
                binding.textView2.text = it.description ?: ""
                binding.textView3.text = formatDateTime(it.startDate ?: "")
                binding.textView4.text = formatDateTime(it.endDate ?: "")
            }
        }

        viewModel.position.observe(viewLifecycleOwner) { position ->
            binding.textView5.text = position?.name ?: ""
        }

        viewModel.structure.observe(viewLifecycleOwner) { structure ->
            binding.textView6.text = structure?.name ?: ""
        }

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
