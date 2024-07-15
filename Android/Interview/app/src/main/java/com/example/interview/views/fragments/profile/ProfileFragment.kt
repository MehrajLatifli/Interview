package com.example.interview.views.fragments.profile

import android.app.AlertDialog
import android.content.Context
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
import coil.load
import coil.transform.CircleCropTransformation
import com.example.interview.R
import com.example.interview.databinding.CustomregistrationresultdialogBinding
import com.example.interview.databinding.FragmentProfileBinding
import com.example.interview.utilities.Constants.API_KEY
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.loadImageWithGlideAndResizeFromUrl
import com.example.interview.utilities.loadUrl
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.profile.ProfileViewModel
import com.example.interview.views.fragments.auth.login.LogInFragmentDirections
import com.example.interview.views.fragments.base.BaseFragment
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.includeProgressbar.progressBar.gone()
        binding.Maincardview.gone()

        viewModel.getprofile()

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {

                binding.includeBottomnav.BottomNavigationView.visible()
                binding.includeProgressbar.progressBar.visible()
            } else {
                lifecycleScope.launch {
                    binding.includeProgressbar.progressBar.gone()
                    delay(1000)
                    binding.Maincardview.visible()
                }
            }
        }



        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->

            if (errorMessage.isNullOrBlank()) {

            } else {

                customregistrationresultdialog(requireContext(),"UnSuccessful!","${errorMessage.toString()}",R.color.MellowMelon)
            }
        }


        viewModel.profiles.observe(viewLifecycleOwner) { items ->
            items.forEach { profile ->
           binding.profileimageView.loadImageWithGlideAndResizeFromUrl(profile.imagePath, requireContext())



                binding.usernametextView.text = profile.username
                binding.emailtextView.text = profile.email
                Log.e("profile", profile.imagePath.toString())
                Log.e("profile", profile.username.toString())
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
}