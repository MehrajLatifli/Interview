package com.example.interview.views.fragments.profile

import android.app.AlertDialog
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.interview.R
import com.example.interview.databinding.CustomresultdialogBinding
import com.example.interview.databinding.FragmentProfileBinding
import com.example.interview.models.responses.get.profile.ProfileResponse
import com.example.interview.utilities.NetworkChangeReceiver
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

    private val networkChangeReceiver = NetworkChangeReceiver { isConnected ->
        if (isAdded && isVisible) {
            handleNetworkStatusChange(isConnected)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.includeProgressbar?.progressBar?.gone()



        binding?.rvuserClaims?.adapter = userClaimAdapder
        binding?.rvroles?.adapter = roleAdapter

        requireContext().registerReceiver(networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))



        binding?.LogOutButton?.setOnClickListener {
            setUserAuth()
            viewModel.logout()
            lifecycleScope.launch {
                delay(250)
                customresultdialog(
                    requireContext(),
                    "Successful!",
                    "Please wait a moment, we are preparing for you...",
                    R.color.DeepPurple
                )
                delay(1500)
                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLogInFragment())
            }

        }



        val themeName = getThemeName() ?: "Primary"
        applyTheme(themeName)

        lifecycleScope.launch {

            applySize(getPrimaryFontsize(), getSecondaryFontsize())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().unregisterReceiver(networkChangeReceiver)
        viewModel.profiles.removeObservers(viewLifecycleOwner)

    }

    private fun handleNetworkStatusChange(isConnected: Boolean) {

        lifecycleScope.launch {
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                binding?.let { bitem ->
                    if (isConnected) {

                        bitem.Maincardview.gone()
                        bitem.NestedScrollView.gone()
                        bitem.LogOutButton.visible()



                        delay(200)


                        viewModel.getprofile()
                        observeData()


                        val profiles = viewModel.getprofile()?: emptyList()
                        val userClaims = viewModel.userclaims.value ?: emptyList()
                        val roles = viewModel.roles.value ?: emptyList()
                        bitem.userClaimstextView.text = "Userclaims:"
                        bitem.rolestextView.text = "Roles:"
                        if (profiles.isNotEmpty()) {
                            profiles.forEach { profile ->

                                val imagePath = profile.imagePath
                                if (imagePath != null && imagePath.isNotBlank()) {

                                    bitem.profileimageView.loadImageWithGlideAndResizeFromUrl(imagePath, requireContext())

                                    bitem.usernametextView.text=profile.username
                                    bitem.emailtextView.text=profile.email

                                } else {

                                    bitem.profileimageView.setImageResource(R.color.MellowMelon)

                                }
                                profile.permitions.forEach { permissions ->
                                    userClaimAdapder.updateList(permissions.userClaims)
                                    roleAdapter.updateList(permissions.roles)
                                }
                            }


                            userClaimAdapder.updateList(userClaims)
                            roleAdapter.updateList(roles)




                            bitem.Maincardview.visible()
                            bitem.NestedScrollView.visible()
                            bitem.LogOutButton.visible()


                        }


                    } else {


                        bitem.Maincardview.gone()
                        bitem.NestedScrollView.gone()
                        bitem.LogOutButton.gone()
                        // When disconnected from the network
                        bitem.userClaimstextView.text = ""
                        bitem.rolestextView.text = ""
                        bitem.usernametextView.text = ""
                        bitem.emailtextView.text = ""



                        userClaimAdapder.updateList(emptyList())
                        roleAdapter.updateList(emptyList())
                    }
                }
            }
            }
    }


    private fun applySize(savedPrimaryFontSize: Float, savedSecondaryFontSize:Float) {


            binding?.let { bitem ->

                lifecycleScope.launch {
                    bitem?.usernametextView?.textSize = savedPrimaryFontSize
                    bitem?.emailtextView?.textSize = savedSecondaryFontSize
                    bitem?.userClaimstextView?.textSize =savedPrimaryFontSize
                    bitem?.rolestextView?.textSize =savedPrimaryFontSize

                }
                lifecycleScope.launch {
                    userClaimAdapder?.setFontSizes(savedPrimaryFontSize, savedSecondaryFontSize)
                }
                lifecycleScope.launch {
                    roleAdapter?.setFontSizes(savedPrimaryFontSize, savedSecondaryFontSize)
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



        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            lifecycleScope.launch {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    binding?.let { bitem ->
                        if (isLoading) {
                            delay(250)
                            bitem?.includeProgressbar?.progressBar?.visible()
                            bitem?.Maincardview?.gone()
                            bitem?.NestedScrollView?.gone()

                            delay(250)
                        } else {
                            bitem?.includeProgressbar?.progressBar?.gone()
                            delay(250)
                            bitem?.apply {
                                Maincardview.visible()
                                NestedScrollView.visible()
                            }
                        }
                    }
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank()) {
                if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    binding?.let {
                        if (viewModel?.profiles?.value?.isNullOrEmpty()==false) {
                            customresultdialog(
                                requireContext(),
                                "UnSuccessful!",
                                errorMessage,
                                R.color.MellowMelon
                            )
                        }
                    }
                }
            }
        }

        viewModel.profiles.observe(viewLifecycleOwner) { items ->
            if (isAdded && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                items.forEach { profile ->
                    profile.let {
                        binding?.let { bitem ->
                            bitem?.profileimageView?.loadImageWithGlideAndResizeFromUrl(
                                profile.imagePath,
                                requireContext()
                            )
                            profile.permitions.forEach { permitions ->
                                permitions.let {
                                    userClaimAdapder.updateList(permitions.userClaims)
                                    roleAdapter.updateList(permitions.roles)
                                }
                            }
                            bitem?.usernametextView?.text = profile.username
                            bitem?.emailtextView?.text = profile.email
                            Log.e("profile", profile.imagePath.toString())
                            Log.e("profile", profile.username.toString())
                        }
                    }
                }
            }
        }
    }

    private fun customresultdialog(context: Context, title: String, text: String, colorId: Int) {
        lifecycleScope.launch(Dispatchers.Main) {
            val dialogBinding = CustomresultdialogBinding.inflate(LayoutInflater.from(context))
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
