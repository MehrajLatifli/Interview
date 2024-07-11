package com.example.interview.views.fragments.auth.registration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.interview.R
import com.example.interview.databinding.FragmentRegistrationBinding
import com.example.interview.models.responses.post.registration.Register
import com.example.interview.viewmodels.auth.AuthViewModel
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.InputStream

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    private val args: RegistrationFragmentArgs by navArgs()
    private val viewModel by viewModels<AuthViewModel>()

    private var selectedFile: File? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView.text = "Create an ${args.accountType} account"

        binding.imageViewEye.setOnClickListener {
            showPassword(binding.editText3, binding.imageViewEye)
        }

        binding.imageViewEye2.setOnClickListener {
            showPassword(binding.editText4, binding.imageViewEye2)
        }

        binding.buttonUploadFile.setOnClickListener {
            openFilePicker()
        }

        binding.buttonRegistration.setOnClickListener {
            val username = binding.editText.text.toString()
            val email = binding.editText2.text.toString()
            val password = binding.editText3.text.toString()
            val phoneNumber = binding.editText4.text.toString()
            val imagePath = selectedFile

            val registerData = Register(
                username = username,
                email = email,
                password = password,
                phoneNumber = phoneNumber,
                imagePath = imagePath
            )

            viewModel.register(registerData)
        }

        // Observe ViewModel LiveData
        viewModel.loading.observe(viewLifecycleOwner) {
            // Show or hide progress indicator
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) {
            // Handle authentication result
        }
    }

    private fun showPassword(editText: EditText, imageView: ImageView) {
        val isPasswordVisible =
            editText.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        editText.inputType = if (isPasswordVisible) {
            imageView.setImageResource(R.drawable.eye)
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            imageView.setImageResource(R.drawable.hidden)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }

        editText.setSelection(editText.text.length)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*" // You can specify a MIME type if needed
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val fileName = getFileName(uri)
                binding.editText6.setText(fileName)
                selectedFile = File(requireContext().cacheDir, fileName)

                // Copy content to cache file
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

    companion object {
        private const val PICK_FILE_REQUEST_CODE = 1
    }
}
