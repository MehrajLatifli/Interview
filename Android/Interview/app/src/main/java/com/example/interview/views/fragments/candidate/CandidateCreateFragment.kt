package com.example.interview.views.fragments.candidate

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.CustomregistrationresultdialogBinding
import com.example.interview.databinding.FragmentCandidateCreateBinding
import com.example.interview.databinding.FragmentCandidateReadBinding
import com.example.interview.models.responses.post.candidatedocument.CandidateDocument
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.candidate.CandidateViewModel
import com.example.interview.views.fragments.auth.login.LogInFragmentDirections
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

            if (!isEmailValid(email)) {
                customRegistrationResultDialog("Unsuccessful!", "Invalid email format", R.color.MellowMelon)
                return@setOnClickListener
            }

            if (selectedFile == null) {
                customRegistrationResultDialog("Unsuccessful!", "Select a file", R.color.MellowMelon)
                return@setOnClickListener
            }

            val registerData = CandidateDocument(
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


    }

    private fun obseveData() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {
                binding.includeProgressbar.progressBar.visible()
            } else {
                binding.includeProgressbar.progressBar.gone()
            }
        }

        viewModel.candidateDocuments.observe(viewLifecycleOwner) { item ->



        }

        viewModel.complateResult.observe(viewLifecycleOwner) { complateResult ->
            lifecycleScope.launch {
                if (complateResult) {
                    customRegistrationResultDialog(
                        "Successful!",
                        "Please wait a moment, we are preparing for you...",
                        R.color.DeepPurple
                    )



                    delay(2500)


                    findNavController().navigate(CandidateCreateFragmentDirections.actionCandidateCreateFragmentToCandidateReadFragment())


                }
                else{
                    delay(2500)
                    findNavController().navigate(CandidateCreateFragmentDirections.actionCandidateCreateFragmentToOperationFragment())
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank()) {
                Log.e("CandidateViewModel", errorMessage)
                customRegistrationResultDialog("Unsuccessful!", errorMessage, R.color.MellowMelon)
            }
        }
    }

    private fun customRegistrationResultDialog(title: String, text: String, colorId: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            val dialogBinding =
                CustomregistrationresultdialogBinding.inflate(LayoutInflater.from(requireContext()))
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

    private fun isEmailValid(email: String): Boolean {
        val emailPattern = "^(?=.{8,100}\$)([a-zA-Z0-9]+[-._+&])*[a-zA-Z0-9]+@([-a-zA-Z0-9]+\\.)+[a-zA-Z]{2,20}\$"
        return email.matches(emailPattern.toRegex())
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
}
