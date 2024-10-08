package com.example.interview.views.fragments.candidate

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentCandidateDetailBinding
import com.example.interview.databinding.FragmentCandidateReadBinding
import com.example.interview.utilities.Constants.FileURL
import com.example.interview.utilities.Constants.HostURL
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.candidate.CandidateViewModel
import com.example.interview.views.adapters.candidate.CandidateAdapter
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidateDetailFragment  : BaseFragment<FragmentCandidateDetailBinding>(
    FragmentCandidateDetailBinding::inflate) {

    private val args: CandidateDetailFragmentArgs by navArgs()
    private val viewModel by viewModels<CandidateViewModel>()

    private lateinit var cvurl: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getCandidateDocumentByID(args.id)

        observeData()

        binding.ViewCVButton.setOnClickListener{

            openFile(cvurl)

        }

        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        applySize(getPrimaryFontsize(), getSecondaryFontsize())
    }

    private fun applySize(savedPrimaryFontSize: Float, savedSecondaryFontSize:Float) {


        lifecycleScope.launch {


            binding.let {

                it.titletextView.textSize = savedPrimaryFontSize
                it.titletextView2.textSize = savedPrimaryFontSize
                it.titletextView3.textSize = savedPrimaryFontSize
                it.titletextView4.textSize = savedPrimaryFontSize
                it.titletextView5.textSize = savedPrimaryFontSize
                it.titletextView6.textSize = savedPrimaryFontSize


                it.textView1.textSize = savedSecondaryFontSize
                it.textView2.textSize = savedSecondaryFontSize
                it.textView3.textSize = savedSecondaryFontSize
                it.textView4.textSize = savedSecondaryFontSize
                it.textView5.textSize = savedSecondaryFontSize
                it.textView6.textSize = savedSecondaryFontSize
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

        viewModel.candidateDocument.observe(viewLifecycleOwner) { item ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                item?.let {
                    binding.textView1.text = it.surname ?: ""
                    binding.textView2.text = it.name ?: ""
                    binding.textView3.text = it.patronymic ?: ""
                    binding.textView4.text = it.phonenumber ?: ""
                    binding.textView5.text = it.email ?: ""
                    binding.textView6.text = it.address ?: ""
                    cvurl = it.cv ?: ""
                }
            }
        }


        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                if (isLoading) {
                    binding.includeProgressbar.progressBar.visible()
                    binding.NestedScrollView.gone()


                } else {
                    binding.includeProgressbar.progressBar.gone()
                    binding.NestedScrollView.visible()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                if (!errorMessage.isNullOrBlank()) {
                    Log.e("CandidateViewModel", errorMessage)
                    customresultdialog("Unsuccessful!", errorMessage, R.color.MellowMelon)

                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.candidateDocument.removeObservers(viewLifecycleOwner)

    }

    private fun customresultdialog(title: String, text: String, colorId: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            val dialogBinding =
                CustomresultdialogBinding.inflate(LayoutInflater.from(requireContext()))
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

    private fun openFile(url: String) {
        if (url.isNotEmpty()) {
            try {
                val replacedUrl = url.replace("http://host.docker.internal", FileURL)
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(replacedUrl), getMimeType(replacedUrl))
                    flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
                }
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Log.e("CandidateDetailFragment", "No application to handle the file. URL: $url")
                customresultdialog("Unsuccessful!", e.localizedMessage, R.color.MellowMelon)
            } catch (e: Exception) {
                Log.e("CandidateDetailFragment", "Error opening file. URL: $url", e)
                customresultdialog("Unsuccessful!", e.localizedMessage, R.color.MellowMelon)
            }
        } else {
            Log.e("CandidateDetailFragment", "Invalid URL: $url")
        }
    }


    private fun getMimeType(url: String): String {
        return when {
            url.endsWith(".pdf") -> "application/pdf"
            url.endsWith(".epub") -> "application/epub+zip"
            url.endsWith(".docx") -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            url.endsWith(".doc")  -> "application/msword"

            else -> "*/*"
        }
    }
}