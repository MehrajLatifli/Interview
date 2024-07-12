package com.example.interview.views.fragments.auth.registration

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.interview.R
import com.example.interview.databinding.CustomregistrationresultdialogBinding
import com.example.interview.databinding.FragmentRegistrationBinding
import com.example.interview.models.responses.post.registration.Register
import com.example.interview.viewmodels.auth.AuthViewModel
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.views.fragments.walkthrough.WalkthroughFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>(FragmentRegistrationBinding::inflate) {

    private val args: RegistrationFragmentArgs by navArgs()
    private val viewModel by viewModels<AuthViewModel>()

    private var selectedFile: File? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.textView.text = "Create an ${args.accountType} account"



        binding.accounttextView.setOnClickListener{
            findNavController().navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLogInFragment())
        }

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
            val confirmpassword = binding.editText4.text.toString()
            val phoneNumber = binding.editText5.text.toString()
            val imagePath = selectedFile


            if (!isUsernameandPasswordValid(username)) {


                customregistrationresultdialog(requireContext(),"UnSuccessful!","Username must have at least 8 characters, one uppercase letter, one lowercase letter, one special character, and one number",R.color.MellowMelon)

                return@setOnClickListener
            }



            if (!isEmailValid(email)) {
                customregistrationresultdialog(requireContext(),"UnSuccessful!","Invalid email format",R.color.MellowMelon)

                return@setOnClickListener
            }

            if (!isUsernameandPasswordValid(password)) {


                customregistrationresultdialog(requireContext(),"UnSuccessful!","Password must have at least 8 characters, one uppercase letter, one lowercase letter, one special character, and one number",R.color.MellowMelon)


                return@setOnClickListener
            }

            if (!isUsernameandPasswordValid(confirmpassword)) {



                customregistrationresultdialog(requireContext(),"UnSuccessful!","Confirm password must have at least 8 characters, one uppercase letter, one lowercase letter, one special character, and one number",R.color.MellowMelon)



                return@setOnClickListener
            }

            if (password != confirmpassword) {


                customregistrationresultdialog(requireContext(),"UnSuccessful!","Password and Confirm Password are not same",R.color.MellowMelon)

                return@setOnClickListener
            }


            if (binding.editText6.text.isNullOrBlank()) {

                customregistrationresultdialog(requireContext(),"UnSuccessful!","Select images",R.color.MellowMelon)

                return@setOnClickListener
            }

            val registerData = Register(
                username = username,
                email = email,
                password = password,
                phoneNumber = phoneNumber,
                imagePath = imagePath
            )

            if (args.accountType.contains("Admin")) {
                viewModel.registerAdmin(registerData)
            }

            if (args.accountType.contains("HR")) {
                viewModel.registerHR(registerData)
            }
        }


        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {

            } else {

            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) { auth ->

            if (auth) {

                customregistrationresultdialog(requireContext(),"Successful!","Please wait a moment, we are preparing for you...",R.color.DeepPurple)
            } else {

            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNullOrBlank()) {
            } else {

                customregistrationresultdialog(requireContext(),"UnSuccessful!","${errorMessage.toString()}",R.color.MellowMelon)
            }
        }


        viewModel.authResult.observe(viewLifecycleOwner) { authResult ->

            authResult?.let {
                if (it) {

                } else {

                }
            }
        }
    }


    private fun customregistrationresultdialog(context: Context, title:String, text:String, colorId: Int) {

        lifecycleScope.launch(Dispatchers.Main) {
            val dialogBinding =
                CustomregistrationresultdialogBinding.inflate(LayoutInflater.from(context))
            val dialog = AlertDialog.Builder(context).apply {
                setView(dialogBinding.root)
            }.create()

            dialogBinding.itemimageView.loadImageWithGlideAndResize(R.drawable.registerstatuse, context)
            dialogBinding.titleTextView.text = title
            dialogBinding.titleTextView.setTextColor(ContextCompat.getColor(context, colorId))
            dialogBinding.textTextView.text = text
            dialogBinding.textTextView.setTextColor(ContextCompat.getColor(context, colorId))

            dialog.show()

            delay(2000)
             dialog.cancel()
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
            type = "image/jpeg, image/png, image/jpg, image/gif"
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->

                if (!isValidImageFile(uri)) {
                    Toast.makeText(requireContext(), "Please select a valid image", Toast.LENGTH_SHORT).show()
                    return
                }

                val fileName = getFileName(uri)
                binding.editText6.setText(fileName+"...")
                selectedFile = File(requireContext().cacheDir, fileName)


                requireContext().contentResolver.openInputStream(uri)?.use { inputStream ->
                    selectedFile?.outputStream()?.use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }
    }

    private fun isUsernameandPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[#?!@\$%^&*-_])(?=.*?[0-9]).{8,}\$"
        return password.matches(passwordPattern.toRegex())
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

    private fun isValidImageFile(uri: Uri): Boolean {
        val type = requireContext().contentResolver.getType(uri)
        return type?.startsWith("image/jpeg") == true || type?.startsWith("image/png") == true  || type?.startsWith("image/jpg") == true  || type?.startsWith("image/gif") == true
    }

    companion object {
        private const val PICK_FILE_REQUEST_CODE = 1
    }
}
