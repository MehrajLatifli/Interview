package com.example.interview.views.fragments.auth.login

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentLogInBinding
import com.example.interview.models.responses.post.login.Login
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.auth.AuthViewModel
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate)  {


    private val viewModel by viewModels<AuthViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        binding.includeProgressbar.progressBar.gone()


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


                customresultdialog(requireContext(),"UnSuccessful!","Username must have at least 8 characters, one uppercase letter, one lowercase letter, one special character, and one number",R.color.MellowMelon)

                return@setOnClickListener
            }



            if (!isUsernameandPasswordValid(password)) {


                customresultdialog(requireContext(),"UnSuccessful!","Password must have at least 8 characters, one uppercase letter, one lowercase letter, one special character, and one number",R.color.MellowMelon)


                return@setOnClickListener
            }


            val login = Login(
                username = username,
                password = password,
            )

            viewModel.login(login) { apiKey, refreshToken ->
                saveCredentials(apiKey, refreshToken)
            }

            if (    getUserAuth()==false) {
                lifecycleScope.launch {
                    delay(1000)
                    binding.mainConstraintLayout.gone()
                    binding.includeProgressbar.progressBar.visible()
                    delay(1000)
                }
            } else {
                lifecycleScope.launch {
                    binding.includeProgressbar.progressBar.gone()
                    binding.mainConstraintLayout.gone()

                    delay(1000)
                }
            }



        }


        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {

            } else {

            }
        }

        viewModel.authResult.observe(viewLifecycleOwner) { auth ->
            lifecycleScope.launch {

                if (auth) {

                    customresultdialog(
                        requireContext(),
                        "Successful!",
                        "Please wait a moment, we are preparing for you...",
                        R.color.DeepPurple
                    )
                    //    findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToHomeFragment())

                        delay(2500)

                        findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToProfileFragment())

                } else {

                    delay(2500)

                    findNavController().navigate(LogInFragmentDirections.actionLogInFragmentToWalkthroughFragment())

                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage.isNullOrBlank()) {
            } else {

                customresultdialog(requireContext(),"UnSuccessful!","${errorMessage.toString()}",R.color.MellowMelon)
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

        viewModel.loginResponses.observe(viewLifecycleOwner) { entities ->
            // Update your UI with the list of entities
            Log.d("Database", "Login entities: $entities")
            // e.g., display in a RecyclerView or other UI components

            entities.forEach {

            }


        }
    }




    private fun customresultdialog(context: Context, title:String, text:String, colorId: Int) {

        lifecycleScope.launch(Dispatchers.Main) {
            val dialogBinding =
                CustomresultdialogBinding.inflate(LayoutInflater.from(context))
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

    private fun getUserAuth(): Boolean {
        val sp = requireActivity().getSharedPreferences("authresult_local", Context.MODE_PRIVATE)
        return sp.getBoolean("isAuth", false)
    }



    private fun saveCredentials(apiKey: String, refreshToken: String) {
        saveApiKey(apiKey)
        saveRefreshToken(refreshToken)
        setUserAuth()
    }

    private fun saveRefreshToken(token: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("refresh_token", token)
            apply()
        }
    }

    private fun saveApiKey(key: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("api_key", key)
            apply()
        }
    }

}