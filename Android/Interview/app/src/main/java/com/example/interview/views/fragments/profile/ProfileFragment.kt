package com.example.interview.views.fragments.profile

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interview.R
import com.example.interview.databinding.CustomregistrationresultdialogBinding
import com.example.interview.databinding.FragmentProfileBinding
import com.example.interview.utilities.gone
import com.example.interview.utilities.loadImageWithGlideAndResize
import com.example.interview.utilities.loadImageWithGlideAndResizeFromUrl
import com.example.interview.utilities.visible
import com.example.interview.viewmodels.profile.ProfileViewModel
import com.example.interview.views.adapters.profile.RoleAdapter
import com.example.interview.views.adapters.profile.UserClaimAdapder
import com.example.interview.views.fragments.auth.login.LogInFragmentDirections
import com.example.interview.views.fragments.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    private val viewModel by viewModels<ProfileViewModel>()

    private val userClaimAdapder = UserClaimAdapder()
    private val roleAdapter = RoleAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.includeProgressbar.progressBar.gone()
        binding.Maincardview.gone()
        binding.NestedScrollView.gone()
        binding.LogOutButton.gone()


        viewModel.getprofile()



        observeData()


        binding.rvuserClaims.adapter = userClaimAdapder
        binding.rvroles.adapter = roleAdapter


        binding.LogOutButton.setOnClickListener{

            setUserAuth()



            viewModel.logout(binding.usernametextView.text.toString())

            lifecycleScope.launch {
                delay(250)
                customregistrationresultdialog(
                    requireContext(),
                    "Successful!",
                    "Please wait a moment, we are preparing for you...",
                    R.color.DeepPurple
                )
                delay(1500)




                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLogInFragment())
            }


        }

    }



    private fun observeData() {

        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading) {
                lifecycleScope.launch {
                    delay(1000)
                    //  binding.includeBottomnav.BottomNavigationView.visible()
                    binding.includeProgressbar.progressBar.visible()
                    delay(1000)
                }
            } else {
                lifecycleScope.launch {
                    binding.includeProgressbar.progressBar.gone()
                    delay(1000)
                    binding.Maincardview.visible()
                    binding.NestedScrollView.visible()
                    binding.LogOutButton.visible()
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
                profile.let {

                    binding.profileimageView.loadImageWithGlideAndResizeFromUrl(profile.imagePath, requireContext())

                    profile.permitions.forEach { permitions ->

                        permitions.let {


                            userClaimAdapder.updateList(permitions.userClaims)
                            roleAdapter.updateList(permitions.roles)
                        }
                    }
                }


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

    private fun setUserAuth() {
        val sp = requireActivity().getSharedPreferences("authresult_local", Context.MODE_PRIVATE)

        sp.edit().putBoolean("isAuth", false).apply()
    }
}