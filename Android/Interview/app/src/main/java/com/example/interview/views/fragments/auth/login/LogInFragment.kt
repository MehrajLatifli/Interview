package com.example.interview.views.fragments.auth.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.CustomregistrationresultdialogBinding
import com.example.interview.databinding.FragmentLogInBinding
import com.example.interview.models.responses.post.login.Login
import com.example.interview.models.responses.post.registration.Register
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.viewmodels.auth.AuthViewModel
import com.example.interview.views.fragments.auth.registration.RegistrationFragmentDirections
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate)  {

    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imageViewEye.setOnClickListener {
            showPassword(binding.editText3, binding.imageViewEye)
        }

        binding.accounttextView.setOnClickListener{
            findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToAccountTypeFragment())
        }



        binding.buttonLogIn.setOnClickListener {
            val username = binding.editText.text.toString()
            val password = binding.editText3.text.toString()



            if (!isUsernameandPasswordValid(username)) {


                customregistrationresultdialog(requireContext(),"UnSuccessful!","Username must have at least 8 characters, one uppercase letter, one lowercase letter, one special character, and one number",R.color.MellowMelon)

                return@setOnClickListener
            }



            if (!isUsernameandPasswordValid(password)) {


                customregistrationresultdialog(requireContext(),"UnSuccessful!","Password must have at least 8 characters, one uppercase letter, one lowercase letter, one special character, and one number",R.color.MellowMelon)


                return@setOnClickListener
            }


            val login = Login(
                username = username,
                password = password,
            )

                viewModel.login(login)

        }


        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {

            } else {

            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) { auth ->
            lifecycleScope.launch {

                if (auth) {

                    customregistrationresultdialog(
                        requireContext(),
                        "Successful!",
                        "Please wait a moment, we are preparing for you...",
                        R.color.DeepPurple
                    )
                    //    findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())

                        delay(2500)

                        findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToProfileFragment())

                } else {

                }
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

                    setUserAuth()

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
            dialog.dismiss()
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




    private fun isUsernameandPasswordValid(password: String): Boolean {
        val passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[#?!@\$%^&*-_])(?=.*?[0-9]).{8,}\$"
        return password.matches(passwordPattern.toRegex())
    }


    private fun setUserAuth() {
        val sp = requireActivity().getSharedPreferences("authresult_local", Context.MODE_PRIVATE)

        sp.edit().putBoolean("isAuth", true).apply()
    }



}