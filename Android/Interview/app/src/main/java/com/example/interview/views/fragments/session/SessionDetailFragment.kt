package com.example.interview.views.fragments.session

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
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
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SessionDetailFragment : BaseFragment<FragmentSessionDetailBinding>(FragmentSessionDetailBinding::inflate) {

    private val args: SessionDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<SessionViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ensure binding is not null and view is created
        binding?.let { bitem ->
            bitem.includeProgressbar.progressBar.visible()
            bitem.NestedScrollView.gone()

            lifecycleScope.launch {
                delay(200)

                args.session.id?.let { sessionId -> viewModel.getSessionByID(sessionId) }
                args.session.vacancyId?.let { vacancyId -> viewModel.getVacancyByID(vacancyId) }
                args.session.candidateId?.let { candidateId -> viewModel.getCandidateDocumentByID(candidateId) }
                args.session.userId?.let { viewModel.getprofile() }

                // Observe data after loading
                observeData()

                // Ensure UI updates are performed on the main thread
                withContext(Dispatchers.Main) {
                    binding?.includeProgressbar?.progressBar?.gone()
                    binding?.NestedScrollView?.visible()
                }
            }
        }
        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        applySize(getPrimaryFontsize(), getSecondaryFontsize())
    }

    override fun onDestroyView() {
        viewModel?.vacancies?.removeObservers(viewLifecycleOwner)
        viewModel?.sessions?.removeObservers(viewLifecycleOwner)
        viewModel?.profiles?.removeObservers(viewLifecycleOwner)
        super.onDestroyView()
    }

    private fun applySize(savedPrimaryFontSize: Float, savedSecondaryFontSize:Float) {


        lifecycleScope.launch {



            binding.titletextView.textSize=savedPrimaryFontSize

            binding.titletextView2.textSize=savedPrimaryFontSize

            binding.titletextView3.textSize=savedPrimaryFontSize

            binding.titletextView4.textSize=savedPrimaryFontSize

            binding.titletextView5.textSize=savedPrimaryFontSize

            binding.titletextView6.textSize=savedPrimaryFontSize


            binding.textView1.textSize=savedSecondaryFontSize

            binding.textView2.textSize=savedSecondaryFontSize

            binding.textView3.textSize=savedSecondaryFontSize

            binding.textView4.textSize=savedSecondaryFontSize

            binding.textView5.textSize=savedSecondaryFontSize

            binding.textView6.textSize=savedSecondaryFontSize


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
            if (themeName == "Secondary") {
                binding.Main.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.Black
                )
                binding.NestedScrollView.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.color.Black
                )
            }
        }
    }


    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }

    private fun observeData() {
        // Check if view is valid before observing
        if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)){

            viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
                // Use 'binding?.let' to ensure binding is valid
                binding?.let { bitem ->
                    if (isLoading) {
                        bitem.includeProgressbar.progressBar.visible()
                        bitem.NestedScrollView.gone()
                    } else {
                        bitem.includeProgressbar.progressBar.gone()
                        bitem.NestedScrollView.visible()
                    }
                }
            }

            viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    if (!errorMessage.isNullOrBlank()) {
                        Log.e("SessionDetailFragment", errorMessage)
                        customresultdialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
                    }
                }
            }

            viewModel.session.observe(viewLifecycleOwner) { item ->
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    item?.let {
                        binding?.let { bitem ->
                            bitem.textView1.text = it.endValue.toString() ?: ""

                            if (!it.startDate.isNullOrEmpty()) {
                                bitem.textView2.text = formatDateTime(it.startDate)
                            }

                            if (!it.endDate.isNullOrEmpty()) {
                                bitem.textView3.text = formatDateTime(it.endDate)
                            }
                        }
                    }
                }
            }

            viewModel.vacancy.observe(viewLifecycleOwner) { vacancy ->
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    binding?.let { bitem ->
                        bitem.textView4.text = vacancy?.title ?: ""
                    }
                }
            }

            viewModel.candidateDocument.observe(viewLifecycleOwner) { candidateDocument ->
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    binding?.let { bitem ->
                        bitem.textView5.text = candidateDocument?.name ?: ""
                    }
                }
            }

            viewModel.profiles.observe(viewLifecycleOwner) { profiles ->
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    val profilesText = profiles.map { profile -> "${profile.username ?: ""}" }
                        .joinToString(separator = "\n\n")

                    binding?.let { bitem ->
                        bitem.textView6.text = profilesText
                    }
                }
            }
        }
    }

    private fun formatDateTime(dateTimeString: String): String {
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

