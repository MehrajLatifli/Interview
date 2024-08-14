package com.example.interview.views.fragments.candidate

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentCandidateCreateBinding
import com.example.interview.models.responses.post.candidatedocument.CandidateDocumentRequest
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.candidate.CandidateViewModel
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class CandidateCreateFragment : BaseFragment<FragmentCandidateCreateBinding>(
    FragmentCandidateCreateBinding::inflate) {

    private var selectedFile: File? = null
    private val viewModel by viewModels<CandidateViewModel>()




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        obseveData()

        binding.buttonUploadFile.setOnClickListener {
            openFilePicker()
        }




        binding.buttonRegistration.setOnClickListener {
            val surname = binding.editText.text.toString()
            val name = binding.editText2.text.toString()
            val patronymic = binding.editText3.text.toString()
            val phonenumber = binding.editText4.text.toString()
            val email = binding.editText5.text.toString()
            val cv = selectedFile
            val address = binding.editText7.text.toString() // Assuming this should be address

            if(name.isNullOrBlank())
            {

                customresultdialog("Unsuccessful!", "Write name", R.color.MellowMelon)
                return@setOnClickListener
            }

            if (!isEmailValid(email)) {
                customresultdialog("Unsuccessful!", "Invalid email format", R.color.MellowMelon)
                return@setOnClickListener
            }

            if (selectedFile == null) {
                customresultdialog("Unsuccessful!", "Select a file", R.color.MellowMelon)
                return@setOnClickListener
            }

            val registerData = CandidateDocumentRequest(
                surname = surname,
                name = name,
                patronymic = patronymic,
                phonenumber = phonenumber,
                email = email,
                cv = cv,
                address = address
            )


            viewModel.registerCandidatedocument(registerData)




        }


        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        applySize(getPrimaryFontsize(), getSecondaryFontsize())
    }

    private fun applySize(savedPrimaryFontSize: Float, savedSecondaryFontSize:Float) {


        lifecycleScope.launch {

            binding.let { bindingitem ->

                bindingitem.editText.textSize = savedPrimaryFontSize
                bindingitem.editText2.textSize = savedPrimaryFontSize
                bindingitem.editText3.textSize = savedSecondaryFontSize

                bindingitem.editText4.textSize = savedPrimaryFontSize
                bindingitem.editText5.textSize = savedPrimaryFontSize
                bindingitem.editText6.textSize = savedSecondaryFontSize
                bindingitem.editText7.textSize = savedSecondaryFontSize
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.complateResult.removeObservers(viewLifecycleOwner)

    }

    private fun applyTheme(themeName: String) {
        lifecycleScope.launch {
            if (themeName == "Secondary") {

                binding.let {
                    it.Main.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.color.Black
                    )
                    it.NestedScrollView.background = ContextCompat.getDrawable(
                        requireContext(),
                        R.color.Black
                    )

                    val hintColor = ContextCompat.getColor(requireContext(), R.color.White)

                    it.editText.setHintTextColor(hintColor)
                    it.editText2.setHintTextColor(hintColor)
                    it.editText3.setHintTextColor(hintColor)
                    it.editText4.setHintTextColor(hintColor)
                    it.editText5.setHintTextColor(hintColor)
                    it.editText6.setHintTextColor(hintColor)
                    it.editText7.setHintTextColor(hintColor)

                    it.editText.setTextColor(hintColor)
                    it.editText2.setTextColor(hintColor)
                    it.editText3.setTextColor(hintColor)
                    it.editText4.setTextColor(hintColor)
                    it.editText5.setTextColor(hintColor)
                    it.editText6.setTextColor(hintColor)
                    it.editText7.setTextColor(hintColor)
                    it.textView.setTextColor(hintColor)
                }
            }
        }
    }

    private fun getThemeName(): String? {
        val sharedPreferences = requireContext().getSharedPreferences("setting_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("themeName", null)
    }

    private fun obseveData() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                if (isLoading) {
                    binding.includeProgressbar.progressBar.visible()
                } else {
                    binding.includeProgressbar.progressBar.gone()
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

        viewModel.candidateDocuments.observe(viewLifecycleOwner) { item ->



        }

        viewModel.complateResult.observe(viewLifecycleOwner) { complateResult ->
            lifecycleScope.launch {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    if (complateResult) {
                        customresultdialog(
                            "Successful!",
                            "Please wait a moment, we are preparing for you...",
                            R.color.DeepPurple
                        )



                        delay(2500)


                        findNavController().navigate(CandidateCreateFragmentDirections.actionCandidateCreateFragmentToCandidateReadFragment())


                    } else {
                        delay(2500)
                        findNavController().navigate(CandidateCreateFragmentDirections.actionCandidateCreateFragmentToOperationFragment())
                    }
                }
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

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*" // Allows selecting any file type
            addCategory(Intent.CATEGORY_OPENABLE) // Ensures it opens file picker
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                if (!isValidFile(uri)) {
                    Toast.makeText(requireContext(), "Please select a valid file", Toast.LENGTH_SHORT).show()
                    return
                }

                val fileName = getFileName(uri)
                binding.editText6.setText(fileName + "...")
                selectedFile = File(requireContext().cacheDir, fileName ?: "unknown_file")

                requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                    selectedFile?.outputStream()?.use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }
    }



    private fun getFileName(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                it.getString(nameIndex)
            } else null
        }
    }

    private fun isValidFile(uri: Uri): Boolean {
        val type = requireContext().contentResolver.getType(uri)
        return type?.startsWith("application/msword") == true ||
                type?.startsWith("application/pdf") == true ||
                type?.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document") == true ||
                type?.startsWith("application/epub+zip") == true
    }



    companion object {
        private const val PICK_FILE_REQUEST_CODE = 1
    }

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "^(?=.{8,100}\$)([a-zA-Z0-9]+[-._+&])*[a-zA-Z0-9]+@([-a-zA-Z0-9]+\\.)+[a-zA-Z]{2,20}\$"
        return email.matches(emailPattern.toRegex())
    }
}
