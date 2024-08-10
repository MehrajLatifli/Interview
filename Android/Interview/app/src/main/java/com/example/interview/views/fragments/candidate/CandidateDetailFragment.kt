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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentCandidateDetailBinding
import com.example.interview.databinding.FragmentCandidateReadBinding
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


    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }

    private fun observeData() {

        viewModel.candidateDocument.observe(viewLifecycleOwner) { item ->
            item?.let {
                binding.textView1.text = it.surname ?: ""
                binding.textView2.text = it.name ?: ""
                binding.textView3.text = it.patronymic ?: ""
                binding.textView4.text = it.phonenumber ?: ""
                binding.textView5.text = it.email ?: ""
                binding.textView6.text = it.address ?: ""
                cvurl=it.cv?: ""
            }
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
                Log.e("CandidateViewModel", errorMessage)
                customresultdialog("Unsuccessful!", errorMessage, R.color.MellowMelon)




            }

        }
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
                val replacedUrl = url.replace("http://host.docker.internal", "http://10.0.2.2")
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