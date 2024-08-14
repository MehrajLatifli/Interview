package com.example.interview.views.fragments.splash

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.interview.R
import com.example.interview.databinding.FragmentSplashBinding
import com.example.interview.views.fragments.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateScreen()
    }

    private fun navigateScreen() {
        lifecycleScope.launch {
            val isAuth = getUserAuth()
            val accessToken = getApiKey()
            val refreshToken = getRefreshToken()
            delay(3000)
            if (isAuth) {
                if (accessToken != null && refreshToken != null) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToHomeFragment())
                }
                else {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToWalkthroughFragment())
                }
            } else {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToWalkthroughFragment())
            }

        }
    }



    private fun getUserAuth(): Boolean {
        val sp = requireActivity().getSharedPreferences("authresult_local", Context.MODE_PRIVATE)
        return sp.getBoolean("isAuth", false)
    }

    private fun getApiKey(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("api_key", null)
    }

    private fun getRefreshToken(): String? {
        val sharedPreferences = requireActivity().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("refresh_token", null)
    }

}